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

    @ExceptionHandler(LeafNotFoundException.class)
    public ResponseEntity<String> handleLeafNotFoundException(LeafNotFoundException e) {
        String msg = gson.toJson(Collections.singletonMap("message", e.getMessage()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body(msg);
    }
}
