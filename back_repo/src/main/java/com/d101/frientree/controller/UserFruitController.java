package com.d101.frientree.controller;


import com.d101.frientree.dto.user.response.UserConfirmationResponse;
import com.d101.frientree.dto.userfruit.request.UserFruitTextRequest;
import com.d101.frientree.dto.userfruit.response.UserFruitCreateResponse;
import com.d101.frientree.dto.userfruit.response.UserFruitSaveResponse;
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

@RestController
@RequestMapping("/user-fruit")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserFruitController {

    private final UserFruitService userFruitService;

    @Operation(summary = "STT 음성 파일", description = "음성파일을 STT해서 감정 분석한 후 3가지 결과가 나옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserFruitCreateResponse.class))),
            @ApiResponse(responseCode = "503", description = "(message : \"Naver API Error\", code : 503)\n" +
                    "\n" +
                    "(message : \"Python AI API Error\", code : 503)")
    })
    @PostMapping(value = "/speech-to-text-audio", consumes = "multipart/form-data")
    public ResponseEntity<UserFruitCreateResponse> speechToTextAudio(@RequestParam("file") MultipartFile file) throws Exception {
        return userFruitService.speechToTextAudio(file);
    }

    @Operation(summary = "STT 음성 파일", description = "음성파일을 STT해서 감정 분석한 후 3가지 결과가 나옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserFruitCreateResponse.class))),
            @ApiResponse(responseCode = "503", description = "(message : \"Naver API Error\", code : 503)\n" +
                    "\n" +
                    "(message : \"Python AI API Error\", code : 503)")
    })
    @PostMapping(value = "/speech-to-text-text")
    public ResponseEntity<UserFruitCreateResponse> speechToTextText(UserFruitTextRequest textFile) throws Exception {
        return userFruitService.speechToTextText(textFile);
    }

    @Operation(summary = "유저 감정 열매 저장", description = "최종적으로 유저가 선택한 감정 열매를 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserFruitSaveResponse.class))),
            @ApiResponse(responseCode = "404", description = "(message : \"Fruit Not Found\", code : 503)"),
            @ApiResponse(responseCode = "500", description = "(message : \"User Modify Exception\", code : 500)"),
    })
    @PostMapping
    public ResponseEntity<UserFruitSaveResponse> userFruitSave(@RequestParam("fruitNum") Long fruitNum){
        return userFruitService.userFruitSave(fruitNum);
    }
}
