package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserCreateStatusResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateStatusConfirmationResponse {

    private String message;
    private UserCreateStatusResponseDTO data;

    public static UserCreateStatusConfirmationResponse createUserCreateStatusConfirmationResponse(String message, UserCreateStatusResponseDTO dto) {
        return UserCreateStatusConfirmationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
