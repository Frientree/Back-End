package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserConfirmationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserConfirmationResponse {

    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private UserConfirmationResponseDTO data;

    public static UserConfirmationResponse createUserConfirmationResponse(String message, UserConfirmationResponseDTO dto) {
        return UserConfirmationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}

