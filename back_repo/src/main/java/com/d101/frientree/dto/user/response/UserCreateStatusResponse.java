package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserCreateStatusResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateStatusResponse {

    private String message;
    private UserCreateStatusResponseDTO data;

    public static UserCreateStatusResponse createUserCreateStatusResponse(String message, UserCreateStatusResponseDTO dto) {
        return UserCreateStatusResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
