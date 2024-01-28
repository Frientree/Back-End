package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeleteResponse {

    private String message;
    private Boolean data;

    public static UserDeleteResponse createUserDeleteResponse(String message, Boolean data) {
        return UserDeleteResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
