package com.d101.frientree.service;


import com.d101.frientree.dto.userfruit.request.UserFruitTextRequest;
import com.d101.frientree.dto.userfruit.response.UserFruitSaveResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserFruitService {
    ResponseEntity<UserFruitSaveResponse> speechToTextAudio(MultipartFile file) throws Exception;

    ResponseEntity<UserFruitSaveResponse> speechToTextText(UserFruitTextRequest textFile) throws Exception;
}
