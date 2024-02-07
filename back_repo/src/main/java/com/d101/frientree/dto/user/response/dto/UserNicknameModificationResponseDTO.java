package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserNicknameModificationResponseDTO {

    private String userNickname;

    public static UserNicknameModificationResponseDTO creatUserNicknameModificationResponseDTO(User user) {
        return UserNicknameModificationResponseDTO.builder()
                .userNickname(user.getUserNickname())
                .build();
    }
}
