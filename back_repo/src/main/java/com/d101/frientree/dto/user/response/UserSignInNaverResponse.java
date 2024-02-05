package com.d101.frientree.dto.user.response;

import com.d101.frientree.dto.user.response.dto.UserSignInNaverResponseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSignInNaverResponse {

    private String message;
    private UserSignInNaverResponseDTO data;

    public static UserSignInNaverResponse createUserSignInNaverResponseDTO(String message, UserSignInNaverResponseDTO dto) {
        return UserSignInNaverResponse.builder()
                .message(message)
                .data(dto)
                .build();
    }
}
