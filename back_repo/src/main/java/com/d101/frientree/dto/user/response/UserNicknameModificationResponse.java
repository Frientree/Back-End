package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserNicknameModificationResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserNicknameModificationResponse {

    private String message;
    private UserNicknameModificationResponseDTO data;

    public static UserNicknameModificationResponse createUserNicknameModificationResponse(String message, UserNicknameModificationResponseDTO dto) {
        return UserNicknameModificationResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
