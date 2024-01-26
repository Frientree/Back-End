package com.d101.frientree.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact();
    }

    public static Map<String, Object> validateToken(String token) {

        Map<String, Object> claim = null;

        try {

            SecretKey key = Keys.hmacShaKeyFor(JwtUtil.key.getBytes("UTF-8"));

            claim = Jwts.parser()
                    .verifyWith(key) // 수정된 부분
                    .build() // JwtParserBuilder에서 JwtParser를 빌드
                    .parseSignedClaims(token)
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
        SecretKey key = new SecretKeySpec(JwtUtil.key.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        Claims claims = Jwts.parser()
                .verifyWith(key) // 수정된 부분
                .build() // JwtParserBuilder에서 JwtParser를 빌드
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration().getTime();
    }

}