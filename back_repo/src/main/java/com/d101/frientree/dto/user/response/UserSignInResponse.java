package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserSignInResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignInResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private UserSignInResponseDTO data;

    public static UserSignInResponse createUserConfirmationResponse(String message, UserSignInResponseDTO dto) {
        return UserSignInResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
