package com.d101.frientree.controller;

import com.d101.frientree.dto.appversion.AppVersionResponse;
import com.d101.frientree.service.AppVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app-version")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AppVersionController {

    private final AppVersionService appVersionService;


    @Operation(summary = "앱 버전 조회", description = "앱 버전을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "앱 버전 조회 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = AppVersionResponse.class))),
    })
    @GetMapping
    public ResponseEntity<AppVersionResponse> appversionConfirm(){
        return appVersionService.confirm();
    }
}
