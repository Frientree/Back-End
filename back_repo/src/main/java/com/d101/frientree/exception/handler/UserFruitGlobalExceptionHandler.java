package com.d101.frientree.exception.handler;

import com.d101.frientree.exception.userfruit.NaverClovaAPIException;
import com.d101.frientree.exception.userfruit.PythonAPIException;
import com.d101.frientree.exception.fruit.FruitNotFoundException;
import com.d101.frientree.exception.userfruit.UserFruitCreateException;
import com.d101.frientree.exception.userfruit.UserFruitNotFoundException;
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
public class UserFruitGlobalExceptionHandler {
    private final Gson gson;
    private static final HttpHeaders JSON_HEADERS;
    static {
        JSON_HEADERS = new HttpHeaders();
        JSON_HEADERS.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    @ExceptionHandler(NaverClovaAPIException.class)
    public ResponseEntity<String> handleNaverClovaAPIException(NaverClovaAPIException e){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(PythonAPIException.class)
    public ResponseEntity<String> handlePythonAPIException(PythonAPIException e){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(FruitNotFoundException.class)
    public ResponseEntity<String> handleFruitNotFoundException(FruitNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(UserFruitNotFoundException.class)
    public ResponseEntity<String> handleUserFruitNotFoundException(UserFruitNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(UserFruitCreateException.class)
    public ResponseEntity<String> handleUserFruitCreateException(UserFruitCreateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    public String stringToGson(String message){
        return gson.toJson(Collections.singletonMap("message", message));
    }
}
