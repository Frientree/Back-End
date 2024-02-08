package com.d101.frientree.dto.message.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

@AllArgsConstructor

public class MessageResponse {
    @Schema(description = "상태 코드", example = "Success")
    private String message;
    @Schema(description = "나무 메시지 내용")
    private String data;

    public static MessageResponse createMessageResponse(String message, String description) {
        return MessageResponse.builder()
                .message(message)
                .data(description)
                .build();
    }

}
