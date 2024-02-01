package com.d101.frientree.service;

import com.d101.frientree.dto.message.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface MessageService {

    ResponseEntity<MessageResponse> confirm();


}
