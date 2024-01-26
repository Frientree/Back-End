package com.d101.frientree.serviceImpl;

import com.d101.frientree.dto.user.UserDTO;
import com.d101.frientree.dto.user.request.*;
import com.d101.frientree.dto.user.response.UserChangeNicknameResponse;
import com.d101.frientree.dto.user.response.UserConfirmationResponse;
import com.d101.frientree.dto.user.response.dto.*;
import com.d101.frientree.entity.RefreshToken;
import com.d101.frientree.entity.User;
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

    @Override
    public UserSignInResponseDTO signIn(UserSignInRequestDTO userSignInRequestDTO) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userSignInRequestDTO.getUserEmail());

        if (!passwordEncoder.matches(userSignInRequestDTO.getUserPw(), userDetails.getPassword())) {
            // TODO: 비밀번호 불일치 커스텀 에러 처리 추가해야 합니다.
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

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

        return new UserSignInResponseDTO(accessToken, refreshToken);
    }

    @Override
    public UserTokenRefreshResponseDTO tokenRefresh(UserTokenRefreshRequestDTO userTokenRefreshRequestDTO) {

        String clientRefreshToken = userTokenRefreshRequestDTO.getRefreshToken();

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
        String newAccessToken = JwtUtil.generateToken(claims, 5);

        if (checkTime(serverRefreshToken.getExpiryDate())) {
            String newRefreshToken = JwtUtil.generateToken(claims, 10);
            Long newRefreshTokenExpiry = JwtUtil.getExpirationDateFromToken(newRefreshToken);
            Instant newRefreshTokenExpiryDate = Instant.ofEpochMilli(newRefreshTokenExpiry);
            DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
            String formattedExpiryDate = formatter.format(newRefreshTokenExpiryDate);
            serverRefreshToken.setRefreshToken(newRefreshToken);
            serverRefreshToken.setExpiryDate(formattedExpiryDate);
            refreshTokenRepository.save(serverRefreshToken);
            return UserTokenRefreshResponseDTO.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }

        return UserTokenRefreshResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(serverRefreshToken.getRefreshToken())
                .build();
    }

    // 유저 닉네임 변경
    @Override
    @Transactional
    public ResponseEntity<UserChangeNicknameResponse> modifyNickname(UserChangeNicknameRequest userChangeNicknameRequest) {

        // 현재 접속한 유저 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // jwt에서 가져온 유저 정보의 username에는 String으로 묶어놓은 userId가 들어가 있습니다.
        String userId = authentication.getName();
        // 유저 정보를 찾을때는 findById에서 userId를 Long으로 바꿔줘야 합니다.
        User currentUser = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));

        currentUser.setUserNickname(userChangeNicknameRequest.getUserNickname());

        UserChangeNicknameResponse response = UserChangeNicknameResponse.createUserChangeNicknameResponse(
                "Success",
                UserChangeNicknameResponseDTO.creatUserChangeNicknameResponseDTO(
                        UserChangeNicknameRequest.createUserChangeNicknameRequest(currentUser)
                ));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 개별 조회
    @Override
    public ResponseEntity<UserConfirmationResponse> confirm(Long id) {
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
        UserConfirmationResponse response = UserConfirmationResponse.createUserConfirmationResponse(
                "Success",
                UserConfirmationResponseDTO.createUserConfirmationResponseDTO(currentUser)
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 유저 전체 조회
    @Override
    public List<UserConfirmationResponseDTO> listConfirm() {
        return null;
    }

    @Override
    public UserCreateResponseDTO createUser(UserCreateRequestDTO userCreateRequestDTO) {

        LocalDateTime userCreateDate = LocalDateTime.now();

        User newUser = User.builder()
                .userNickname(userCreateRequestDTO.getUserNickname())
                .userPassword(passwordEncoder.encode(userCreateRequestDTO.getUserPw()))
                .userEmail(userCreateRequestDTO.getUserEmail())
                .userCreateDate(Date.from(userCreateDate.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        userRepository.save(newUser);

        return UserCreateResponseDTO.builder()
                .isCreated(true)
                .build();
    }

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

}


