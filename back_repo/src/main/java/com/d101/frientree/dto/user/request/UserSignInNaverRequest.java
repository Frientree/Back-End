package com.d101.frientree.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignInNaverRequest {
    @Schema(description = "네이버 소셜 유저 고유 식별번호", example = "DwL69DAQnc4ne5RIQ1-lYfMMvsM_UDu_uQGfo3A")
    private String code;
}
