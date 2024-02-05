package com.d101.frientree.dto.user.response.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignInNaverResponseDTO {

    private String accessToken;

    private String refreshToken;

    public static UserSignInNaverResponseDTO createUserSignInNaverResponseDTO(String accessToken, String refreshToken) {
        return UserSignInNaverResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
