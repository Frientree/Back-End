package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserProfileConfirmationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileConfirmationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private UserProfileConfirmationResponseDTO data;

    public static UserProfileConfirmationResponse createUserProfileConfirmationResponse(String message, UserProfileConfirmationResponseDTO dto) {
        return UserProfileConfirmationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
