package com.d101.frientree.exception.handler;

import com.d101.frientree.exception.user.*;
import com.d101.frientree.util.CustomJwtException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import com.d101.frientree.exception.userfruit.NaverClovaAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
@RequiredArgsConstructor
public class UserGlobalExceptionHandler {

    private final Gson gson;

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        String msg = gson.toJson(Collections.singletonMap("message", e.getMessage()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body(msg);
    }

    @ExceptionHandler(PasswordNotMatchingException.class)
    public ResponseEntity<String> handlePasswordNotMatchingException(PasswordNotMatchingException e) {
        String msg = gson.toJson(Collections.singletonMap("message", e.getMessage()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).body(msg);
    }

    @ExceptionHandler(EmailDuplicatedException.class)
    public ResponseEntity<String> handleEmailDuplicatedException(EmailDuplicatedException e) {
        String msg = gson.toJson(Collections.singletonMap("message", e.getMessage()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(msg);
    }

    @ExceptionHandler(NicknameValidateException.class)
    public ResponseEntity<String> handleNicknameValidateException(NicknameValidateException e) {
        String msg = gson.toJson(Collections.singletonMap("message", e.getMessage()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(msg);
    }

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<String> handleCustomJwtException(CustomJwtException e) {
        String msg = gson.toJson(Collections.singletonMap("message", e.getMessage()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).body(msg);
    }

    @ExceptionHandler(EmailCodeNotMatchingException.class)
    public ResponseEntity<String> handleEmailCodeNotMatchingException(EmailCodeNotMatchingException e) {
        String msg = gson.toJson(Collections.singletonMap("message", e.getMessage()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(msg);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<String> handleCustomValidationException(CustomValidationException e) {
        String msg = gson.toJson(Collections.singletonMap("message", e.getMessage()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(msg);
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<String> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException e) {
        String msg = gson.toJson(Collections.singletonMap("message", e.getMessage()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(msg);
    }

    @ExceptionHandler(NaverClovaAPIException.class)
    public ResponseEntity<String> handleNaverClovaAPIException(NaverClovaAPIException e){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }

}
