package com.d101.frientree.serviceImpl.message;


import com.d101.frientree.dto.message.response.MessageResponse;
import com.d101.frientree.entity.message.Message;
import com.d101.frientree.repository.message.MessageRepository;
import com.d101.frientree.service.message.MessageService;
import com.d101.frientree.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final CommonUtil commonUtil;


    @Override
    public ResponseEntity<MessageResponse> confirm() {
        commonUtil.checkServerInspectionTime();

        Optional<Message> messages = messageRepository.findRandomMessage();
        MessageResponse response =
                MessageResponse.createMessageResponse(
                        "Success",
                        messages.get().getMessageDescription()
                );
        return ResponseEntity.ok(response);
    }
}
