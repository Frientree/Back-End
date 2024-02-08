package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserTokenRefreshGenerationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTokenRefreshGenerationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private UserTokenRefreshGenerationResponseDTO data;

    public static UserTokenRefreshGenerationResponse createUserTokenRefreshGenerationResponse(String message, UserTokenRefreshGenerationResponseDTO dto) {
        return UserTokenRefreshGenerationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
