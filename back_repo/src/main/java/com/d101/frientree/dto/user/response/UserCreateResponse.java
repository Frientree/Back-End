package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateResponse {

    private String message;
    private boolean data;

    public static UserCreateResponse createUserCreateResponse(String message, boolean data) {
        return UserCreateResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
