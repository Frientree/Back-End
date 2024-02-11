package com.d101.frientree.service.juice;

import com.d101.frientree.dto.juice.request.JuiceGenerationRequest;
import com.d101.frientree.dto.juice.response.JuiceConfirmationResponse;
import com.d101.frientree.dto.juice.response.JuiceGenerationResponse;
import com.d101.frientree.dto.juice.response.JuiceListConfirmationResponse;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

public interface JuiceService {
    ResponseEntity<JuiceListConfirmationResponse> listConfirm();

    ResponseEntity<JuiceConfirmationResponse> confirm(Long juiceId);

    ResponseEntity<JuiceGenerationResponse> generate(JuiceGenerationRequest juiceGenerationRequest) throws ParseException;
}
