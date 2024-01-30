package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSendEmailCertificationResponse {

    private String message;
    private Boolean data;

    public static UserSendEmailCertificationResponse createUserEmailCertificationResponse(String message, Boolean data) {
        return UserSendEmailCertificationResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
