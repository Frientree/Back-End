package com.d101.frientree.controller;


import com.d101.frientree.dto.userfruit.request.UserFruitTextRequest;
import com.d101.frientree.dto.userfruit.response.UserFruitCreateResponse;
import com.d101.frientree.dto.userfruit.response.UserFruitSaveResponse;
import com.d101.frientree.service.UserFruitService;
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

    @PostMapping(value = "/speech-to-text-audio", consumes = "multipart/form-data")
    public ResponseEntity<UserFruitCreateResponse> speechToTextAudio(@RequestParam("file") MultipartFile file) throws Exception {
        return userFruitService.speechToTextAudio(file);
    }

    @PostMapping(value = "/speech-to-text-text")
    public ResponseEntity<UserFruitCreateResponse> speechToTextText(UserFruitTextRequest textFile) throws Exception {
        return userFruitService.speechToTextText(textFile);
    }

    @PostMapping
    public ResponseEntity<UserFruitSaveResponse> userFruitSave(@RequestParam("fruitNum") Long fruitNum){
        return userFruitService.userFruitSave(fruitNum);
    }
}
