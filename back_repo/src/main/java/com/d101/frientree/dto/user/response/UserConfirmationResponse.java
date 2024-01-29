package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserConfirmationResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserConfirmationResponse {

    private String message;
    private UserConfirmationResponseDTO data;

    public static UserConfirmationResponse createUserConfirmationResponse(String message, UserConfirmationResponseDTO dto) {
        return UserConfirmationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}

