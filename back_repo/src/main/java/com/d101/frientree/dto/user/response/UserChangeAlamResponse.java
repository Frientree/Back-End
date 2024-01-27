package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChangeAlamResponse {

    private String message;
    private Boolean data;

    public static UserChangeAlamResponse createUserChangeAlamResponse(String message, Boolean data) {
        return UserChangeAlamResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
