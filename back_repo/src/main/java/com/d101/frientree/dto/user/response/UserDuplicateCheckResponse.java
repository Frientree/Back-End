package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDuplicateCheckResponse {

    private String message;
    private Boolean data;

    public static UserDuplicateCheckResponse createUserDuplicateCheckResponse(String message, Boolean data) {
        return UserDuplicateCheckResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
