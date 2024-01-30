package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserPassEmailCertificationResponse {

    private String message;
    private Boolean data;

    public static UserPassEmailCertificationResponse createUserPassEmailCertificationResponse(String message, Boolean data) {
        return UserPassEmailCertificationResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
