package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserSignInResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignInResponse {

    private String message;
    private UserSignInResponseDTO data;

    public static UserSignInResponse createUserConfirmationResponse(String message, UserSignInResponseDTO dto) {
        return UserSignInResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
