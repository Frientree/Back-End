package com.d101.frientree.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFcmTokenUpdateResponse {
    private String message;
    private boolean data;

    public static UserFcmTokenUpdateResponse createUserFcmTokenUpdateResponse(String message, boolean data){
        return UserFcmTokenUpdateResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
