package com.d101.frientree.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTokenRefreshResponseDTO {

    private String accessToken;

    private String refreshToken;
}
