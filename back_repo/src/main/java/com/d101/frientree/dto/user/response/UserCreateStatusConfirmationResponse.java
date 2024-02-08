package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserCreateStatusResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateStatusConfirmationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private UserCreateStatusResponseDTO data;

    public static UserCreateStatusConfirmationResponse createUserCreateStatusConfirmationResponse(String message, UserCreateStatusResponseDTO dto) {
        return UserCreateStatusConfirmationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
