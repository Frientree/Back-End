package com.d101.frientree.controller;


import com.d101.frientree.dto.userfruit.request.UserFruitTextRequest;
import com.d101.frientree.dto.userfruit.response.UserFruitCreateResponse;
import com.d101.frientree.dto.userfruit.response.UserFruitSaveResponse;
import com.d101.frientree.dto.userfruit.response.UserFruitTodayInfoResponse;
import com.d101.frientree.service.UserFruitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user-fruit")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserFruitController {

    private final UserFruitService userFruitService;

    @Operation(summary = "STT 음성 파일 감정 분석", description = "음성파일을 STT해서 감정 분석한 후 3가지 결과가 나옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserFruitCreateResponse.class))),
            @ApiResponse(responseCode = "404", description = "(message : \"m4a Not Found\", code : 404)", content = @Content),
            @ApiResponse(responseCode = "409", description = "(message : \"Already produced fruit\", code : 409)", content = @Content),
            @ApiResponse(responseCode = "500", description = "(message : \"Aws Server Error\", code : 500)", content = @Content),
            @ApiResponse(responseCode = "503", description = """
                    (message : "Naver API Error", code : 503)

                    (message : "Python AI API Error", code : 503)""", content = @Content),
    })
    @PostMapping(value = "/speech-to-text-audio", consumes = "multipart/form-data")
    public CompletableFuture<ResponseEntity<UserFruitCreateResponse>> speechToTextAudio(@RequestParam("file") MultipartFile file) throws Exception {
        return userFruitService.speechToTextAudio(file);
    }

    @Operation(summary = "Text 파일 감정 분석", description = "Text를 감정 분석한 후 3가지 결과가 나옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserFruitCreateResponse.class))),
            @ApiResponse(responseCode = "409", description = "(message : \"Already produced fruit\", code : 409)", content = @Content),
            @ApiResponse(responseCode = "503", description = """
                    (message : "Naver API Error", code : 503)

                    (message : "Python AI API Error", code : 503)""", content = @Content)
    })
    @PostMapping(value = "/speech-to-text-text")
    public ResponseEntity<UserFruitCreateResponse> speechToTextText(@RequestBody UserFruitTextRequest textFile) throws Exception {
        return userFruitService.speechToTextText(textFile);
    }

    @Operation(summary = "유저 감정 열매 저장", description = "최종적으로 유저가 선택한 감정 열매를 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserFruitSaveResponse.class))),
            @ApiResponse(responseCode = "404", description = "(message : \"Fruit Not Found\", code : 404)", content = @Content),
            @ApiResponse(responseCode = "500", description = "(message : \"User Modify Exception\", code : 500)", content = @Content),
    })
    @PostMapping
    public ResponseEntity<UserFruitSaveResponse> userFruitSave(@RequestParam("fruitNum") Long fruitNum){
        return userFruitService.userFruitSave(fruitNum);
    }

    @Operation(summary = "금일 유저 열매 조회", description = "금일 유저가 생성한 열매 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserFruitTodayInfoResponse.class))),
            @ApiResponse(responseCode = "404", description = """
                    (message : "User Not Found", code : 404)
                    
                    (message : "User Fruit Not Found", code : 404)""", content = @Content)
    })
    @GetMapping
    public ResponseEntity<UserFruitTodayInfoResponse> userFruitTodayInfo(@RequestHeader("Date") String CreateDate){
        return userFruitService.userFruitTodayInfo(CreateDate);
    }
}
