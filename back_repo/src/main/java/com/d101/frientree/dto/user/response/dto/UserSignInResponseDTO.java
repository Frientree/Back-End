package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.dto.user.request.UserSignInRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UserSignInResponseDTO {

    private String accessToken;

    private String refreshToken;

    public static UserSignInResponseDTO createUserSignInResponseDTO(String accessToken, String refreshToken) {
        return UserSignInResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
