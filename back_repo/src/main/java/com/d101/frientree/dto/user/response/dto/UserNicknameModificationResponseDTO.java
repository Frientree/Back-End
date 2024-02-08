package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserNicknameModificationResponseDTO {
    @Schema(description = "닉네임", example = "nickname")
    private String userNickname;

    public static UserNicknameModificationResponseDTO creatUserNicknameModificationResponseDTO(User user) {
        return UserNicknameModificationResponseDTO.builder()
                .userNickname(user.getUserNickname())
                .build();
    }
}
