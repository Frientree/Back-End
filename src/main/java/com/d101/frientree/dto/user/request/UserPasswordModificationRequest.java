package com.d101.frientree.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordModificationRequest {
    @Schema(description = "현재 패스워드", example = "qwer1234!")
    private String userPw;
    @Schema(description = "변경 패스워드", example = "qwer4321!")
    private String newPw;

}
