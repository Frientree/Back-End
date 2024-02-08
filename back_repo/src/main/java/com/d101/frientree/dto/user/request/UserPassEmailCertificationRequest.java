package com.d101.frientree.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPassEmailCertificationRequest {
    @Schema(description = "이메일", example = "testuser@email.com")
    private String userEmail;
    @Schema(description = "인증코드", example = "a1b2c3")
    private String code;
}
