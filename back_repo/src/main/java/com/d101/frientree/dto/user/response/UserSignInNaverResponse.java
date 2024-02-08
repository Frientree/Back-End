package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserSignInNaverResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignInNaverResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private UserSignInNaverResponseDTO data;

    public static UserSignInNaverResponse createUserSignInNaverResponseDTO(String message, UserSignInNaverResponseDTO dto) {
        return UserSignInNaverResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
