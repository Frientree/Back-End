package com.d101.frientree.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGenerationRequest {
    @Schema(description = "이메일", example = "testuser@email.com")
    private String userEmail;
    @Schema(description = "비밀번호", example = "qwer1234!")
    private String userPw;
    @Schema(description = "닉네임", example = "nickname")
    private String userNickname;

}
