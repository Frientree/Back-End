package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserTokenRefreshGenerationResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTokenRefreshGenerationResponse {

    private String message;
    private UserTokenRefreshGenerationResponseDTO data;

    public static UserTokenRefreshGenerationResponse createUserTokenRefreshGenerationResponse(String message, UserTokenRefreshGenerationResponseDTO dto) {
        return UserTokenRefreshGenerationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
