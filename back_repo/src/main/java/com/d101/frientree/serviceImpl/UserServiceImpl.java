package com.d101.frientree.serviceImpl;

import com.d101.frientree.dto.user.UserDTO;
import com.d101.frientree.dto.user.request.*;
import com.d101.frientree.dto.user.response.*;
import com.d101.frientree.dto.user.response.dto.*;
import com.d101.frientree.dto.user.response.UserListConfirmationResponse;
import com.d101.frientree.entity.RefreshToken;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.PasswordNotMatchingException;
import com.d101.frientree.exception.UserNotFoundException;
import com.d101.frientree.repository.RefreshTokenRepository;
import com.d101.frientree.repository.UserRepository;
import com.d101.frientree.security.CustomUserDetailsService;
import com.d101.frientree.service.UserService;
import com.d101.frientree.util.CustomJwtException;
import com.d101.frientree.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    // 로그인 + 토큰 발급 로직
    @Override
    public ResponseEntity<UserSignInResponse> signIn(UserSignInRequest userSignInRequest) {

        // 유저 정보를 가져오고, 이메일 불일치시 404 예외처리
        UserDetails userDetails;
        try {
            User currentUser = userRepository.findByUserEmail(userSignInRequest.getUserEmail())
                    .orElseThrow();
            userDetails = customUserDetailsService.loadUserByUsername(String.valueOf(currentUser.getUserId()));
        } catch (UsernameNotFoundException e) {
            throw new UserNotFoundException();
        }

        // 패스워드 불일치시 401 예외처리
        if (!passwordEncoder.matches(userSignInRequest.getUserPw(), userDetails.getPassword())) {
            throw new PasswordNotMatchingException();
        }

        // Jwt 토큰 발급 로직
        Map<String, Object> claims = new HashMap<>();
        UserDTO userDTO = (UserDTO) userDetails;
        Collection<GrantedAuthority> roleNames = userDTO.getAuthorities();
        claims.put("roleNames", roleNames);
        claims.put("username", userDetails.getUsername());

        String accessToken = JwtUtil.generateToken(claims, 10);
        String refreshToken = JwtUtil.generateToken(claims, 60 * 24);

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
    public ResponseEntity<UserTokenRefreshResponse> tokenRefresh(UserTokenRefreshRequest userTokenRefreshRequest) {

        String clientRefreshToken = userTokenRefreshRequest.getRefreshToken();

        if (clientRefreshToken == null) {
            throw new CustomJwtException("NULL_REFRESH");
        }

        Optional<RefreshToken> refreshTokenOptional =
                refreshTokenRepository.findById(clientRefreshToken);

        if (refreshTokenOptional.isEmpty()) {
            throw new CustomJwtException("INVALID_REFRESH");
        }

        RefreshToken serverRefreshToken = refreshTokenOptional.get();
        String username = serverRefreshToken.getUsername();

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        claims.put("roleNames", userDetails.getAuthorities());
        String newAccessToken = JwtUtil.generateToken(claims, 10);

        if (checkTime(serverRefreshToken.getExpiryDate())) {
            String newRefreshToken = JwtUtil.generateToken(claims, 60 * 24);
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

        UserProfileConfirmationResponse response = UserProfileConfirmationResponse.createUserProfileConfirmationResponse(
                "Success",
                UserProfileConfirmationResponseDTO.createUserProfileConfirmationResponseDTO(currentUser)
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

    // 유저 개별 조회
    @Override
    public ResponseEntity<UserConfirmationResponse> confirm(Long id) {

        User currentUser = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

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

        LocalDateTime userCreateDate = LocalDateTime.now();
        User newUser = User.builder()
                .userNickname(userCreateRequest.getUserNickname())
                .userPassword(passwordEncoder.encode(userCreateRequest.getUserPw()))
                .userEmail(userCreateRequest.getUserEmail())
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

        // 1시간 이하로 남았으면 true 반환
        return leftMin <= 60 && !duration.isNegative();
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        return userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));
    }
}


