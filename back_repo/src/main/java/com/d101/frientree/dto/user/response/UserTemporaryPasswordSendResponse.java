package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTemporaryPasswordSendResponse {

    private String message;
    private Boolean data;

    public static UserTemporaryPasswordSendResponse createUserTemporaryPasswordSendResponse(String message, Boolean data) {
        return UserTemporaryPasswordSendResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
