package com.d101.frientree.service;

import com.d101.frientree.dto.terms.response.TermsResponse;
import org.springframework.http.ResponseEntity;

public interface TermsService {
    ResponseEntity<TermsResponse> confirm();

}
