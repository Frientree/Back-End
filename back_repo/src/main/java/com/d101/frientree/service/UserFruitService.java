package com.d101.frientree.service;


import com.d101.frientree.dto.userfruit.request.UserFruitTextRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserFruitService {
    ResponseEntity<?> speechToTextAudio(MultipartFile file) throws Exception;

    ResponseEntity<?> speechToTextText(UserFruitTextRequest textFile) throws Exception;
}
