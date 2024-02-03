package com.d101.frientree.controller;

import com.d101.frientree.dto.terms.response.TermsResponse;
import com.d101.frientree.entity.app.Terms;
import com.d101.frientree.service.TermsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TermsController {

    private final TermsService termsService;


    @GetMapping
    public ResponseEntity<TermsResponse> termsConfirm(){
        return termsService.confirm();
    }
}
