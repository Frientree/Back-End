package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeactivationResponse {

    private String message;
    private Boolean data;

    public static UserDeactivationResponse createUserDeactivationResponse(String message, Boolean data) {
        return UserDeactivationResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
