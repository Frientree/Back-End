package com.d101.frientree.exception.handler;

import com.d101.frientree.exception.server.ServerInspectionTimeException;
import com.d101.frientree.exception.userfruit.NaverClovaAPIException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@RestControllerAdvice
@RequiredArgsConstructor
public class ServerGlobalExceptionHandler {
    private final Gson gson;
    private static final HttpHeaders JSON_HEADERS;
    static {
        JSON_HEADERS = new HttpHeaders();
        JSON_HEADERS.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    @ExceptionHandler(ServerInspectionTimeException.class)
    public ResponseEntity<String> handleNaverClovaAPIException(ServerInspectionTimeException e){
        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }
    public String stringToGson(String message){
        return gson.toJson(Collections.singletonMap("message", message));
    }
}
