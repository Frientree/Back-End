package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserConfirmationResponseDTO {

    @Schema(description = "유저 번호", example = "1")
    private Long userId;
    @Schema(description = "닉네임", example = "nickname")
    private String userNickname;
    @Schema(description = "탈퇴 여부", example = "false")
    private boolean userDisabled;
    @Schema(description = "가입 날짜", example = "2023-12-01")
    private Date userCreateDate;
    @Schema(description = "이파리 생성 가능 여부", example = "true")
    private boolean userLeafStatus;
    @Schema(description = "유저 이메일", example = "testuser1@email.com")
    private String userEmail;
    @Schema(description = "알림 on/off 여부", example = "true")
    private boolean userNotification;
    @Schema(description = "열매 생성 가능 여부", example = "true")
    private boolean userFruitStatus;

    public static UserConfirmationResponseDTO createUserConfirmationResponseDTO(User user) {
        return UserConfirmationResponseDTO.builder()
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userDisabled(user.getUserDisabled())
                .userLeafStatus(user.getUserLeafStatus())
                .userNotification(user.getUserNotification())
                .userFruitStatus(user.getUserFruitStatus())
                .userCreateDate(user.getUserCreateDate())
                .userEmail(user.getUserEmail())
                .build();
    }

}
