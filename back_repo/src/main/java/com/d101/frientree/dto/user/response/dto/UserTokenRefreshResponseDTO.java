package com.d101.frientree.dto.user.response.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserTokenRefreshResponseDTO {

    private String accessToken;
    private String refreshToken;

    public static UserTokenRefreshResponseDTO createUserTokenRefreshResponseDTO(String accessToken, String refreshToken) {
        return UserTokenRefreshResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
