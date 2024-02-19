package com.d101.frientree.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserGenerationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private boolean data;

    public static UserGenerationResponse createUserGenerationResponse(String message, boolean data) {
        return UserGenerationResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
