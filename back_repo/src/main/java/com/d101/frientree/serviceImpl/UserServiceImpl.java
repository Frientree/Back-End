package com.d101.frientree.serviceImpl;

import com.d101.frientree.dto.user.UserDTO;
import com.d101.frientree.dto.user.request.*;
import com.d101.frientree.dto.user.response.*;
import com.d101.frientree.dto.user.response.dto.*;
import com.d101.frientree.entity.EmailCode;
import com.d101.frientree.entity.RefreshToken;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.user.*;
import com.d101.frientree.repository.EmailCodeRepository;
import com.d101.frientree.repository.RefreshTokenRepository;
import com.d101.frientree.repository.UserRepository;
import com.d101.frientree.security.CustomUserDetailsService;
import com.d101.frientree.service.UserService;
import com.d101.frientree.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JavaMailSender javaMailSender;
    private final EmailCodeRepository emailCodeRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final int VERIFICATION_CODE_LENGTH = 6;

    @Value("${jasypt.encryptor.aes256}")
    private String aesKey;

    // 로그인 + 토큰 발급 로직
    @Override
    public ResponseEntity<UserSignInResponse> signIn(UserSignInRequest userSignInRequest) {

        // 유저 정보를 가져오고, 이메일 불일치시 404 예외처리
        UserDetails userDetails;

        String hashedEmail = getAESEncoded(userSignInRequest.getUserEmail());
        User currentUser = userRepository.findByUserEmail(hashedEmail).orElseThrow(() -> new UserNotFoundException("User not found"));

        userDetails = customUserDetailsService.loadUserByUsername(String.valueOf(currentUser.getUserId()));
        // 패스워드 불일치시 401 예외처리
        if (!passwordEncoder.matches(userSignInRequest.getUserPw(), userDetails.getPassword())) {
            throw new PasswordNotMatchingException("password not match");
        }

        // Jwt 토큰 발급 로직
        Map<String, Object> claims = new HashMap<>();
        UserDTO userDTO = (UserDTO) userDetails;
        Collection<GrantedAuthority> roleNames = userDTO.getAuthorities();
        claims.put("roleNames", roleNames);
        claims.put("username", userDetails.getUsername());

        String accessToken = JwtUtil.generateToken(claims, 60 * 24);
        String refreshToken = JwtUtil.generateToken(claims, 43200);

        Long refreshTokenExpiry = JwtUtil.getExpirationDateFromToken(refreshToken);

        Instant refreshTokenExpiryDate = Instant.ofEpochMilli(refreshTokenExpiry);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
        String formattedExpiryDate = formatter.format(refreshTokenExpiryDate);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .refreshToken(refreshToken)
                .username(userDetails.getUsername())
                .expiryDate(formattedExpiryDate)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        UserSignInResponse response = UserSignInResponse.createUserConfirmationResponse(
                "Login Success",
                UserSignInResponseDTO.createUserSignInResponseDTO(accessToken, refreshToken));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 토큰 재발급 로직
    @Override
    public ResponseEntity<UserTokenRefreshResponse> tokenRefreshGenerate(UserTokenRefreshRequest userTokenRefreshRequest) {

        String clientRefreshToken = userTokenRefreshRequest.getRefreshToken();

        Optional<RefreshToken> refreshTokenOptional =
                refreshTokenRepository.findById(clientRefreshToken);

        if (refreshTokenOptional.isEmpty()) {
            throw new RefreshTokenNotFoundException("refresh token error");
        }

        RefreshToken serverRefreshToken = refreshTokenOptional.get();
        String username = serverRefreshToken.getUsername();

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        claims.put("roleNames", userDetails.getAuthorities());
        String newAccessToken = JwtUtil.generateToken(claims, 60 * 24);

        if (checkTime(serverRefreshToken.getExpiryDate())) {
            String newRefreshToken = JwtUtil.generateToken(claims, 43200);
            Long newRefreshTokenExpiry = JwtUtil.getExpirationDateFromToken(newRefreshToken);
            Instant newRefreshTokenExpiryDate = Instant.ofEpochMilli(newRefreshTokenExpiry);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
            String formattedExpiryDate = formatter.format(newRefreshTokenExpiryDate);
            serverRefreshToken.setRefreshToken(newRefreshToken);
            serverRefreshToken.setExpiryDate(formattedExpiryDate);
            refreshTokenRepository.save(serverRefreshToken);
            UserTokenRefreshResponse response = UserTokenRefreshResponse.createUserTokenRefreshResponse(
                    "Success",
                    UserTokenRefreshResponseDTO.createUserTokenRefreshResponseDTO(newAccessToken, newRefreshToken)
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        UserTokenRefreshResponse response = UserTokenRefreshResponse.createUserTokenRefreshResponse(
                "Success",
                UserTokenRefreshResponseDTO.createUserTokenRefreshResponseDTO(newAccessToken, serverRefreshToken.getRefreshToken())
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 닉네임 변경
    @Override
    @Transactional
    public ResponseEntity<UserChangeNicknameResponse> modifyNickname(UserChangeNicknameRequest userChangeNicknameRequest) {

        User currentUser = getUser();

        if (userChangeNicknameRequest.getUserNickname() == null || userChangeNicknameRequest.getUserNickname().isEmpty()) {
            throw new NicknameValidateException("nickname valid error");
        }

        if (userChangeNicknameRequest.getUserNickname().length() > 8) {
            throw new NicknameValidateException("nickname valid error");
        }

        currentUser.setUserNickname(userChangeNicknameRequest.getUserNickname());

        UserChangeNicknameResponse response = UserChangeNicknameResponse.createUserChangeNicknameResponse(
                "Success",
                UserChangeNicknameResponseDTO.creatUserChangeNicknameResponseDTO(currentUser));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 프로필 조회
    @Override
    @Transactional
    public ResponseEntity<UserProfileConfirmationResponse> profileConfirm() {

        User currentUser = getUser();

        String decodeEmail = getAESDecoded(currentUser.getUserEmail());

        UserProfileConfirmationResponse response = UserProfileConfirmationResponse.createUserProfileConfirmationResponse(
                "Success",
                UserProfileConfirmationResponseDTO.createUserProfileConfirmationResponseDTO(currentUser, decodeEmail)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 알림 설정
    @Override
    @Transactional
    public ResponseEntity<UserChangeAlamResponse> modifyAlam(UserChangeAlamRequest userChangeAlamRequest) {

        User currentUser = getUser();
        currentUser.setUserNotification(userChangeAlamRequest.isNotification());

        UserChangeAlamResponse response = UserChangeAlamResponse.createUserChangeAlamResponse(
                "Success",
                true
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 삭제
    @Override
    @Transactional
    public ResponseEntity<UserDeleteResponse> removal() {

        User currentUser = getUser();
        userRepository.delete(currentUser);

        UserDeleteResponse response = UserDeleteResponse.createUserDeleteResponse(
                "Success",
                true
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 비활성화
    @Override
    @Transactional
    public ResponseEntity<UserDeactivateResponse> deactivate() {

        User currentUser = getUser();
        currentUser.setUserDisabled(true);

        UserDeactivateResponse response = UserDeactivateResponse.createUserDeactivateResponse(
                "Success",
                true
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 이메일 중복체크
    @Override
    public ResponseEntity<UserDuplicateCheckResponse> duplicateCheck(UserDuplicateCheckRequest userDuplicateCheckRequest) {

        if (!userDuplicateCheckRequest.getUserEmail().matches("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$")) {
            throw new CustomValidationException("email valid error");
        }

        String hashingEmail = getAESEncoded(userDuplicateCheckRequest.getUserEmail());

        if (userRepository.findByUserEmail(hashingEmail).isPresent()) {
            throw new EmailDuplicatedException("email is duplicate");
        }

        UserDuplicateCheckResponse response = UserDuplicateCheckResponse.createUserDuplicateCheckResponse(
                "Success",
                true
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 인증 이메일 발송
    @Override
    @Transactional
    public ResponseEntity<UserSendEmailCertificationResponse> sendEmailCertificate(UserSendEmailCertificationRequest userSendEmailCertificationRequest) {

        if (!userSendEmailCertificationRequest.getUserEmail().matches("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$")) {
            throw new CustomValidationException("email valid error");
        }

        String hashingEmail = getAESEncoded(userSendEmailCertificationRequest.getUserEmail());

        if (userRepository.findByUserEmail(hashingEmail).isPresent()) {
            throw new EmailDuplicatedException("email is duplicate");
        }

        sendVerificationEmail(userSendEmailCertificationRequest.getUserEmail());

        UserSendEmailCertificationResponse response = UserSendEmailCertificationResponse.createUserEmailCertificationResponse(
                "Success",
                true
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 이메일 인증 처리
    @Override
    public ResponseEntity<UserPassEmailCertificationResponse> passEmailCertificate(UserPassEmailCertificationRequest userPassEmailCertificationRequest) {

        String userEmail = userPassEmailCertificationRequest.getUserEmail();
        String code = userPassEmailCertificationRequest.getCode();

        String redisKey = "userEmail:" + userEmail;

        Object storedCodeObj = redisTemplate.opsForHash().get(redisKey, "code");

        String storedCode = (storedCodeObj != null) ? storedCodeObj.toString() : null;

        if (code.equals(storedCode)) {
            UserPassEmailCertificationResponse response = UserPassEmailCertificationResponse.createUserPassEmailCertificationResponse(
                    "Success",
                    true
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            throw new EmailCodeNotMatchingException("email code not match");
        }
    }

    // 패스워드 변경
    @Transactional
    @Override
    public ResponseEntity<UserChangePasswordResponse> passwordModify(UserChangePasswordRequest userChangePasswordRequest) {

        User currentUser = getUser();

        if (!passwordEncoder.matches(userChangePasswordRequest.getUserPw(), currentUser.getUserPassword())) {
            throw new CustomValidationException("current password not match");
        }

        if (!userChangePasswordRequest.getNewPw().matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!~#$%^&*?])(?!.*[^!~#$%^&*?a-zA-Z0-9]).{8,16}$")) {
            throw new CustomValidationException("password valid error");
        }

        currentUser.setUserPassword(passwordEncoder.encode(userChangePasswordRequest.getNewPw()));

        UserChangePasswordResponse response = UserChangePasswordResponse.createUserChangePasswordResponse(
                "Success",
                true
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 임시 비밀번호 이메일로 발송
    @Override
    @Transactional
    public ResponseEntity<UserTemporaryPasswordSendResponse> temporaryPasswordSend(UserTemporaryPasswordSendRequest userTemporaryPasswordSendRequest) {

        String hashingEmail = getAESEncoded(userTemporaryPasswordSendRequest.getUserEmail());

        User currentUser = userRepository.findByUserEmail(hashingEmail)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        // 임시 비밀번호 생성
        String temporaryPassword = generatePassword();
        sendTemporaryPasswordEmail(userTemporaryPasswordSendRequest.getUserEmail(), temporaryPassword);

        currentUser.setUserPassword(passwordEncoder.encode(temporaryPassword));

        UserTemporaryPasswordSendResponse response = UserTemporaryPasswordSendResponse.createUserTemporaryPasswordSendResponse(
                "Success",
                true
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 이파리, 열매 생성 가능 여부 조회
    @Override
    public ResponseEntity<UserCreateStatusResponse> createStatusConfirm() {

        User currentUser = getUser();

        UserCreateStatusResponse result = UserCreateStatusResponse.createUserCreateStatusResponse(
                "Success",
                UserCreateStatusResponseDTO.createUserCreateStatusResponseDTO(currentUser)
        );
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Override
    public void updateAllUserFruitAndLeafStatus() {
        userRepository.incrementAllUserFruitAndLeafStatus();
    }


    // 유저 개별 조회
    @Override
    public ResponseEntity<UserConfirmationResponse> confirm(Long id) {

        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        UserConfirmationResponse response = UserConfirmationResponse.createUserConfirmationResponse(
                "Success",
                UserConfirmationResponseDTO.createUserConfirmationResponseDTO(currentUser)
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 전체 조회
    @Override
    public ResponseEntity<UserListConfirmationResponse> listConfirm() {

        List<User> users = userRepository.findAll();
        UserListConfirmationResponse response = UserListConfirmationResponse.createUserListConfirmationResponse(
                "Success",
                users
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 가입
    @Override
    public ResponseEntity<UserCreateResponse> generateUser(UserCreateRequest userCreateRequest) {
        // 입력 정보의 유효성 검증
        validateUserCreateRequest(userCreateRequest);

        // 유저 정보 생성
        LocalDateTime userCreateDate = LocalDateTime.now();
        User newUser = User.builder()
                .userNickname(userCreateRequest.getUserNickname())
                .userPassword(passwordEncoder.encode(userCreateRequest.getUserPw()))
                // 이메일 값을 해싱하여 저장합니다.
                .userEmail(getAESEncoded(userCreateRequest.getUserEmail()))
                .userCreateDate(Date.from(userCreateDate.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        userRepository.save(newUser);

        UserCreateResponse result = UserCreateResponse.createUserCreateResponse(
                "Sign-up Success",
                true
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // refresh 토큰의 유효기간 체크
    private boolean checkTime(String exp) {

        // JWT exp를 Instant로 파싱
        Instant expInstant = Instant.parse(exp);

        // Instant를 OffsetDateTime으로 변환
        OffsetDateTime expDateTime = expInstant.atOffset(ZoneOffset.UTC);

        // 현재 시간과의 차이 계산 - Duration 사용
        Duration duration = Duration.between(OffsetDateTime.now(ZoneOffset.UTC), expDateTime);

        // 분 단위 계산
        long leftMin = duration.toMinutes();

        // 하루 이하로 남았으면 true 반환
        return leftMin <= 1440 && !duration.isNegative();
    }

    // 현재 접속한 유저 정보를 가져오는 메서드
    private User getUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        User currentUser = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException("user not found"));

        if (currentUser.getUserDisabled()) {
            throw new UserNotFoundException("user disabled");
        }

        return currentUser;
    }

    private void sendVerificationEmail(String email) {
        // 생성된 토큰을 이용하여 이메일 본문에 포함시킬 URL 생성
        String code = generateRandomCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("프렌트리 이메일 인증 확인");
        message.setText("앱에서 이메일 인증 코드를 입력하세요:\n" + code);

        javaMailSender.send(message);

        emailCodeRepository.save(new EmailCode(email, code));
    }

    private void sendTemporaryPasswordEmail(String email, String newPassword) {
        // 생성된 토큰을 이용하여 이메일 본문에 포함시킬 URL 생성

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("프렌트리 임시 비밀번호 발급");
        message.setText("새로운 임시 비밀번호 입니다 ㅎㅎ\n" + newPassword);

        javaMailSender.send(message);
    }

    private String generateRandomCode() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[VERIFICATION_CODE_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0, VERIFICATION_CODE_LENGTH);
    }

    private void validateUserCreateRequest(UserCreateRequest userCreateRequest) {
        if (userCreateRequest.getUserNickname() == null || userCreateRequest.getUserNickname().isEmpty()) {
            throw new CustomValidationException("nickname valid error");
        }

        if (!userCreateRequest.getUserEmail().matches("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$")) {
            throw new CustomValidationException("email valid error");
        }

        if (!userCreateRequest.getUserPw().matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!~#$%^&*?])(?!.*[^!~#$%^&*?a-zA-Z0-9]).{8,16}$")) {
            throw new CustomValidationException("password valid error");
        }

        if (userCreateRequest.getUserNickname().length() > 8) {
            throw new CustomValidationException("nickname");
        }
    }

    private String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!~#$%^&*?";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(16);

        // 숫자 1개, 대문자 1개, 소문자 1개, 특수 문자 1개를 먼저 추가합니다.
        sb.append("Aa1!");

        // 나머지 12개의 문자를 랜덤하게 생성합니다.
        for (int i = 0; i < 12; i++) {
            sb.append(characters.charAt(rnd.nextInt(characters.length())));
        }

        // 생성된 문자열을 무작위로 섞습니다.
        List<Character> charList = new ArrayList<>();
        for (char c : sb.toString().toCharArray()) {
            charList.add(c);
        }
        Collections.shuffle(charList);

        // 셔플된 문자열을 다시 합칩니다.
        StringBuilder shuffledString = new StringBuilder();
        for (char c : charList) {
            shuffledString.append(c);
        }

        return shuffledString.toString();
    }


    public String getAESEncoded(String data) {
        try {
            String key = "insert 16 length";
            String iv = "insert 16 length";

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Error occurred while encrypting data", e);
        }
    }

    public String getAESDecoded(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(aesKey.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] original = cipher.doFinal(decodedBytes);

            return new String(original, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Error occurred while decrypting data", e);
        }
    }

}


