package com.d101.frientree.util;

import com.d101.frientree.exception.user.CustomJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtUtil {

    private static final String key = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";

    public static String generateToken(Map<String, Object> valueMap, int min) {

        SecretKey key = null;

        try {
            key = Keys.hmacShaKeyFor(JwtUtil.key.getBytes("UTF-8"));

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return Jwts.builder()
                .claim("typ", "JWT") // 헤더 설정
                .claims(valueMap) // 클레임 설정
                .issuedAt(Date.from(ZonedDateTime.now().toInstant())) // 발행 시간 설정
                .expiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) // 만료 시간 설정
                .signWith(key) // 서명 설정
                .compact(); // 토큰 생성
    }

    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claim = null;

        try {

            SecretKey key = Keys.hmacShaKeyFor(JwtUtil.key.getBytes("UTF-8"));

            claim = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token) // 파싱 및 검증, 실패 시 에러
                    .getPayload();

        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJwtException("MalFormed");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJwtException("Expired");
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJwtException("Invalid");
        } catch (JwtException jwtException) {
            throw new CustomJwtException("JWTError");
        } catch (Exception e) {
            throw new CustomJwtException("Error");
        }
        return claim;
    }

    public static Long getExpirationDateFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(JwtUtil.key.getBytes(StandardCharsets.UTF_8)); // UTF-8 인코딩 명시
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration().getTime();
    }

}