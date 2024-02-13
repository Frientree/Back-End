package com.d101.frientree.security.filter;

import com.d101.frientree.dto.user.UserDTO;
import com.d101.frientree.exception.user.CustomJwtException;
import com.d101.frientree.util.JwtUtil;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Log4j2
public class JwtCheckFilter extends OncePerRequestFilter {

    // 해당 경로들은 jwt 토큰 체크를 진행하지 않고 통과시킨다
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        // Preflight 요청은 체크하지 않음
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();

        if (path.equals("/users/sign-in")) {
            return true;
        }

        if (path.equals("/users/sign-up")) {
            return true;
        }

        if (path.startsWith("/v3/api-docs")) {
            return true;
        }

        if (path.startsWith("/swagger-ui")) {
            return true;
        }

        if (path.startsWith("/swagger-resources")) {
            return true;
        }

        if (path.equals("/users/tokens-refresh")) {
            return true;
        }

        if (path.equals("/users/entirety")) {
            return true;
        }

        if (path.matches("/users/\\d+")) {
            return true;
        }

        if (path.equals("/users/id-duplicate")) {
            return true;
        }

        if (path.equals("/users/certification-send")) {
            return true;
        }

        if (path.equals("/users/certification-pass")) {
            return true;
        }

        if (path.equals("/users/temporary-password")) {
            return true;
        }

        if (path.equals("/users/sign-in-naver")) {
            return true;
        }

        if (path.equals("/terms")){
            return true;
        }

        if (path.equals("/app-version")){
            return true;
        }

        if (path.equals("/s3")){
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {

        String authHeaderStr = request.getHeader("Authorization");
        if (authHeaderStr == null || !authHeaderStr.startsWith("Bearer ")) {
            // 적절하지 않은 Authorization 헤더일 경우 401 에러 반환
            sendUnauthorizedError(response);
            return;
        }

        // Bearer // 7 Jwt 문자열
        try {
            String accessToken;

            accessToken = authHeaderStr.substring(7);

            Map<String, Object> claims = JwtUtil.validateToken(accessToken);

            String username = (String) claims.get("username");

            Object roleNamesObj = claims.get("roleNames");
            List<String> roleNames = new ArrayList<>();

            if (roleNamesObj instanceof List<?>) {
                for (Object item : (List<?>) roleNamesObj) {
                    if (item instanceof Map<?, ?> authMap) {
                        Object authority = authMap.get("authority");
                        if (authority instanceof String) {
                            roleNames.add((String) authority);
                        }
                    }
                }
            }

            UserDTO userDTO = new UserDTO(username, "", roleNames);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDTO, null, userDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("An error occurred during sign-in process", e);

            Gson gson = new Gson();
            String msg = gson.toJson(Collections.singletonMap("message", "Fail"));

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
        }

    }

    private void sendUnauthorizedError(HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        String msg = gson.toJson(Collections.singletonMap("message", "Fail"));

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(msg);
        printWriter.close();
    }

}