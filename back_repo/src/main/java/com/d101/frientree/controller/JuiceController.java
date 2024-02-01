package com.d101.frientree.controller;

import com.d101.frientree.dto.juice.request.JuiceGenerationRequest;
import com.d101.frientree.dto.juice.response.JuiceConfirmationResponse;
import com.d101.frientree.dto.juice.response.JuiceGenerationResponse;
import com.d101.frientree.dto.juice.response.JuiceListConfirmationResponse;
import com.d101.frientree.service.JuiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/juice")
@CrossOrigin("*")
@RequiredArgsConstructor
public class JuiceController {

    private final JuiceService juiceService;

    @GetMapping("/entirety")
    public ResponseEntity<JuiceListConfirmationResponse> juiceListConfirmation() {
        return juiceService.listConfirm();
    }

    @GetMapping("/{juiceId}")
    public ResponseEntity<JuiceConfirmationResponse> juiceConfirmation(@PathVariable Long juiceId) {
        return juiceService.confirm(juiceId);
    }
}
