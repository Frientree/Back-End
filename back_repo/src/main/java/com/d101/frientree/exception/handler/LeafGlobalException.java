package com.d101.frientree.exception.handler;

import com.d101.frientree.exception.leaf.LeafNotFoundException;
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
public class LeafGlobalException {

    private final Gson gson;
    private static final HttpHeaders JSON_HEADERS;
    static {
        JSON_HEADERS = new HttpHeaders();
        JSON_HEADERS.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    public String stringToGson(String message){
        return gson.toJson(Collections.singletonMap("message", message));
    }

    // LeafNotFoundException
    @ExceptionHandler(LeafNotFoundException.class)
    // handleLeafNotFoundException(LeafNotFoundException e)
    public ResponseEntity<String> handleLeafNotFoundException(LeafNotFoundException e) {
        //HttpStatus.NOT_FOUND 만 바꾸면서 반복 내보내는 대로
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .headers(JSON_HEADERS)
                .body(stringToGson(e.getMessage()));
    }
}
