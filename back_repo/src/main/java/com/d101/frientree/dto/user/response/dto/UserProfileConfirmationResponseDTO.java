package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserProfileConfirmationResponseDTO {

    @Schema(description = "유저 번호", example = "1")
    private Long userId;
    @Schema(description = "닉네임", example = "nickname")
    private String userNickname;
    @Schema(description = "이파리 생성 가능 여부", example = "true")
    private boolean userLeafStatus;
    @Schema(description = "유저 이메일", example = "testuser1@email.com")
    private String userEmail;
    @Schema(description = "열매 생성 가능 여부", example = "true")
    private boolean userFruitStatus;
    @Schema(description = "소셜 가입 여부", example = "false")
    private boolean social;

    public static UserProfileConfirmationResponseDTO createUserProfileConfirmationResponseDTO(User user, String email, Boolean social) {
        return UserProfileConfirmationResponseDTO.builder()
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userLeafStatus(user.getUserLeafStatus())
                .userFruitStatus(user.getUserFruitStatus())
                .userEmail(email)
                .social(social)
                .build();
    }
}
