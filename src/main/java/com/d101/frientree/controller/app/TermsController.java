package com.d101.frientree.controller.app;

import com.d101.frientree.dto.terms.response.TermsResponse;
import com.d101.frientree.service.app.TermsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TermsController {

    private final TermsService termsService;


    @Operation(summary = "이용 약관 조회", description = "이용약관을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이용약관 조회 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = TermsResponse.class))),
    })
    @GetMapping
    public ResponseEntity<TermsResponse> termsConfirm(){
        return termsService.confirm();
    }
}
