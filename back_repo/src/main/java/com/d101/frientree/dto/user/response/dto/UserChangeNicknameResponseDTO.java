package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.dto.user.request.UserChangeNicknameRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChangeNicknameResponseDTO {

    private String userNickname;

    public static UserChangeNicknameResponseDTO creatUserChangeNicknameResponseDTO(UserChangeNicknameRequest request) {
        return UserChangeNicknameResponseDTO.builder()
                .userNickname(request.getUserNickname())
                .build();
    }
}
