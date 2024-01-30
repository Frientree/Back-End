package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChangePasswordResponse {

    private String message;
    private Boolean data;

    public static UserChangePasswordResponse createUserChangePasswordResponse(String message, Boolean data) {
        return UserChangePasswordResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
