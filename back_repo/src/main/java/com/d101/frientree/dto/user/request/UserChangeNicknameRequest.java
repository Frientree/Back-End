package com.d101.frientree.dto.user.request;

import com.d101.frientree.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangeNicknameRequest {

    private String userNickname;

    public static UserChangeNicknameRequest createUserChangeNicknameRequest(User user) {
        return UserChangeNicknameRequest.builder()
                .userNickname(user.getUserNickname())
                .build();
    }
}
