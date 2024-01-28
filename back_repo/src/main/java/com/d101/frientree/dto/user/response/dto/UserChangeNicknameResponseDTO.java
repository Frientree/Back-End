package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChangeNicknameResponseDTO {

    private String userNickname;

    public static UserChangeNicknameResponseDTO creatUserChangeNicknameResponseDTO(User user) {
        return UserChangeNicknameResponseDTO.builder()
                .userNickname(user.getUserNickname())
                .build();
    }
}
