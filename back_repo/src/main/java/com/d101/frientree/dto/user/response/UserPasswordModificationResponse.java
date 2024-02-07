package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPasswordModificationResponse {

    private String message;
    private Boolean data;

    public static UserPasswordModificationResponse createUserPasswordModificationResponse(String message, Boolean data) {
        return UserPasswordModificationResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
