package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserGenerationResponse {

    private String message;
    private boolean data;

    public static UserGenerationResponse createUserGenerationResponse(String message, boolean data) {
        return UserGenerationResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
