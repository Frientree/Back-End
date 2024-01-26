package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserChangeNicknameResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChangeNicknameResponse {

    private String message;
    private UserChangeNicknameResponseDTO data;

    public static UserChangeNicknameResponse createUserChangeNicknameResponse(String message, UserChangeNicknameResponseDTO dto) {
        return UserChangeNicknameResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
