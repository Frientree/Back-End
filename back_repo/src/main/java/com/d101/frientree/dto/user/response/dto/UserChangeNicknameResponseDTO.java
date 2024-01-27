package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.dto.user.request.UserChangeNicknameRequest;
import com.d101.frientree.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
