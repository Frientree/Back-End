package com.d101.frientree.exception.handler;

import com.d101.frientree.exception.awss3.AwsS3FileNotFoundException;
import com.d101.frientree.exception.awss3.AwsS3InternalServerErrorException;
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
public class AwsS3GlobalExceptionHandler {
    private final Gson gson;
    private static final HttpHeaders JSON_HEADERS;
    static {
        JSON_HEADERS = new HttpHeaders();
        JSON_HEADERS.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    @ExceptionHandler(AwsS3FileNotFoundException.class)
    public ResponseEntity<String> handleAwsS3FileNotFoundException(AwsS3FileNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    @ExceptionHandler(AwsS3InternalServerErrorException.class)
    public ResponseEntity<String> handleAwsS3InternalServerErrorException(AwsS3InternalServerErrorException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }

    public String stringToGson(String message){
        return gson.toJson(Collections.singletonMap("message", message));
    }

}
