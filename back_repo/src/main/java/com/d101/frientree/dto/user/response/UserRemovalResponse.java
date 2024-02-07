package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRemovalResponse {

    private String message;
    private Boolean data;

    public static UserRemovalResponse createUserRemovalResponse(String message, Boolean data) {
        return UserRemovalResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
