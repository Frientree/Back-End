package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserNicknameModificationResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserNicknameModificationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private UserNicknameModificationResponseDTO data;

    public static UserNicknameModificationResponse createUserNicknameModificationResponse(String message, UserNicknameModificationResponseDTO dto) {
        return UserNicknameModificationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
