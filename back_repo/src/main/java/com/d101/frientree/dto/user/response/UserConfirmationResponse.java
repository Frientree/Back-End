package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.userdto.UserConfirmationRequestDTO;
import com.d101.frientree.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserConfirmationResponse {
    private String message;
    private UserConfirmationRequestDTO data;

    public static UserConfirmationResponse createUserConfirmationResponse(String message, UserConfirmationRequestDTO dto){
        return UserConfirmationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}

