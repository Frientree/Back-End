package com.d101.frientree.exception.handler;

import com.d101.frientree.exception.user.*;
import com.d101.frientree.exception.user.CustomJwtException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
@RequiredArgsConstructor
public class UserGlobalExceptionHandler {

    private final Gson gson;
    private static final HttpHeaders JSON_HEADERS;
    static {
        JSON_HEADERS = new HttpHeaders();
        JSON_HEADERS.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(PasswordNotMatchingException.class)
    public ResponseEntity<String> handlePasswordNotMatchingException(PasswordNotMatchingException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(EmailDuplicatedException.class)
    public ResponseEntity<String> handleEmailDuplicatedException(EmailDuplicatedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(NicknameValidateException.class)
    public ResponseEntity<String> handleNicknameValidateException(NicknameValidateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<String> handleCustomJwtException(CustomJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(EmailCodeNotMatchingException.class)
    public ResponseEntity<String> handleEmailCodeNotMatchingException(EmailCodeNotMatchingException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<String> handleCustomValidationException(CustomValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    public ResponseEntity<String> handleRefreshTokenNotFoundException(RefreshTokenNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(UserModifyException.class)
    public ResponseEntity<String> handleUserModifyException(UserModifyException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }
    public String stringToGson(String message){
        return gson.toJson(Collections.singletonMap("message", message));
    }

}
