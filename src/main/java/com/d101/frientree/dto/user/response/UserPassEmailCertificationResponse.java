package com.d101.frientree.dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserPassEmailCertificationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private Boolean data;

    public static UserPassEmailCertificationResponse createUserPassEmailCertificationResponse(String message, Boolean data) {
        return UserPassEmailCertificationResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
