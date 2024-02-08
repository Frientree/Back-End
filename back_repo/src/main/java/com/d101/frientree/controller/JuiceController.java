package com.d101.frientree.controller;

import com.d101.frientree.dto.juice.request.JuiceGenerationRequest;
import com.d101.frientree.dto.juice.response.JuiceConfirmationResponse;
import com.d101.frientree.dto.juice.response.JuiceGenerationResponse;
import com.d101.frientree.dto.juice.response.JuiceListConfirmationResponse;
import com.d101.frientree.service.JuiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/juice")
@CrossOrigin("*")
@RequiredArgsConstructor
public class JuiceController {

    private final JuiceService juiceService;

    @Operation(summary = "주스 생성", description = "해당 주차에 새로운 주스를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "(message : \"Success\", code : 201)",
                    content = @Content(schema = @Schema(implementation = JuiceGenerationResponse.class))),
            @ApiResponse(responseCode = "401", description = """
                    (message : "Access Token Error", code : 401)""", content = @Content),
            @ApiResponse(responseCode = "422", description = """
                    (message : "Input Data Error", code : 422)""", content = @Content),
            @ApiResponse(responseCode = "409", description = """
                    (message : "Date Error", code : 409)""", content = @Content)
    })
    @PostMapping
    public ResponseEntity<JuiceGenerationResponse> juiceGeneration(@RequestBody JuiceGenerationRequest juiceGenerationRequest) throws ParseException {
        return juiceService.generate(juiceGenerationRequest);
    }

    @Operation(summary = "전체 주스 조회", description = "도감 정보를 불러옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "(message : \"Success\", code : 201)",
                    content = @Content(schema = @Schema(implementation = JuiceListConfirmationResponse.class))),
            @ApiResponse(responseCode = "401", description = """
                    (message : "Access Token Error", code : 401)""", content = @Content)
    })
    @GetMapping("/entirety")
    public ResponseEntity<JuiceListConfirmationResponse> juiceListConfirmation() {
        return juiceService.listConfirm();
    }

    @Operation(summary = "개별 주스 조회", description = "개별 주스의 상세정보를 불러옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "(message : \"Success\", code : 201)",
                    content = @Content(schema = @Schema(implementation = JuiceConfirmationResponse.class))),
            @ApiResponse(responseCode = "401", description = """
                    (message : "Access Token Error", code : 401)""", content = @Content),
            @ApiResponse(responseCode = "400", description = """
                    (message : "Juice Not Found", code : 400)""", content = @Content)
    })
    @GetMapping("/{juiceId}")
    public ResponseEntity<JuiceConfirmationResponse> juiceConfirmation(@PathVariable Long juiceId) {
        return juiceService.confirm(juiceId);
    }
}
