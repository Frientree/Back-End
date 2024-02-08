package com.d101.frientree.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTemporaryPasswordSendMailRequest {

    @Schema(description = "이메일", example = "testuser1@email.com")
    private String userEmail;
}
