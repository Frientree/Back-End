package com.d101.frientree.service;


import com.d101.frientree.dto.userfruit.request.UserFruitTextRequest;
import com.d101.frientree.dto.userfruit.response.UserFruitCreateResponse;
import com.d101.frientree.dto.userfruit.response.UserFruitSaveResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserFruitService {
    ResponseEntity<UserFruitCreateResponse> speechToTextAudio(MultipartFile file) throws Exception;

    ResponseEntity<UserFruitCreateResponse> speechToTextText(UserFruitTextRequest textFile) throws Exception;

    ResponseEntity<UserFruitSaveResponse> userFruitSave(Long fruitNum);
}
