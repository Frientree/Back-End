package com.d101.frientree.serviceImpl;


import com.d101.frientree.dto.message.response.MessageResponse;
import com.d101.frientree.entity.message.Message;
import com.d101.frientree.repository.MessageRepository;
import com.d101.frientree.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;


    @Override
    public ResponseEntity<MessageResponse> confirm() {
        List<Message> messages = messageRepository.findAll();

        Random random = new Random();
        int randomIndex = random.nextInt(messages.size());

        Message selectedMessage = messages.get(randomIndex);
        MessageResponse response = MessageResponse.createMessageResponse("Success", selectedMessage.getMessageDescription());

        return ResponseEntity.ok(response);

        }
    }
