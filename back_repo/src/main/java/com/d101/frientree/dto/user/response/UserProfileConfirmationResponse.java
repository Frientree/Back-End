package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserProfileConfirmationResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileConfirmationResponse {

    private String message;
    private UserProfileConfirmationResponseDTO data;

    public static UserProfileConfirmationResponse createUserProfileConfirmationResponse(String message, UserProfileConfirmationResponseDTO dto) {
        return UserProfileConfirmationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
