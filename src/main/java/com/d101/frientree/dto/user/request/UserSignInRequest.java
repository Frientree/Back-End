package com.d101.frientree.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInRequest {
    @Schema(description = "이메일", example = "testuser1@email.com")
    private String userEmail;
    @Schema(description = "패스워드", example = "qwer1234!")
    private String userPw;
}
