package com.d101.frientree.dto.user.response.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserTokenRefreshGenerationResponseDTO {

    private String accessToken;
    private String refreshToken;

    public static UserTokenRefreshGenerationResponseDTO createUserTokenRefreshGenerationResponseDTO(String accessToken, String refreshToken) {
        return UserTokenRefreshGenerationResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
