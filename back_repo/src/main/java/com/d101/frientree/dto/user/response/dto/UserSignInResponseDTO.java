package com.d101.frientree.dto.user.response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSignInResponseDTO {

    private String accessToken;

    private String refreshToken;
}
