package com.d101.frientree.dto.message.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

@AllArgsConstructor

public class MessageResponse {
    private String message;
    private String description;

    public static MessageResponse createMessageResponse(String message, String description) {
        return MessageResponse.builder()
                .message(message)
                .description(description)
                .build();
    }

}
