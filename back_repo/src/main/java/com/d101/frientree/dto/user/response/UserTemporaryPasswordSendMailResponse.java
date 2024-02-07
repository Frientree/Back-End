package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTemporaryPasswordSendMailResponse {

    private String message;
    private Boolean data;

    public static UserTemporaryPasswordSendMailResponse createUserTemporaryPasswordSendMailResponse(String message, Boolean data) {
        return UserTemporaryPasswordSendMailResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
