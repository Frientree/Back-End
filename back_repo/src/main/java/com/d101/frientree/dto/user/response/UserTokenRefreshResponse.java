package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserTokenRefreshResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTokenRefreshResponse {

    private String message;
    private UserTokenRefreshResponseDTO data;

    public static UserTokenRefreshResponse createUserTokenRefreshResponse(String message, UserTokenRefreshResponseDTO dto) {
        return UserTokenRefreshResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
