package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAlamModificationResponse {

    private String message;
    private Boolean data;

    public static UserAlamModificationResponse createUserAlamModificationResponse(String message, Boolean data) {
        return UserAlamModificationResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
