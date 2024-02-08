package com.d101.frientree.dto.user.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserTokenRefreshGenerationResponseDTO {

    @Schema(description = "access token", example = "eyJhbGciOiJIUzUxMiJ9.eyJ0eXAiOiJKV1QiLCJyb2xlTmFtZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwidXNlcm5hbWUiOiI1MCIsImlhdCI6MTcwNzM2ODM1MywiZXhwIjoxNzA3NDU0NzUzfQ.A8JYHX1TBXXSPYo9EjFN91V65wS1Bxl--7g8fWAo0aPigOf94JrYAUCUi9Qs7fS8Ej2WZ4BxRv3Yc4R23QbNbA")
    private String accessToken;
    @Schema(description = "refresh token", example = "eyJhbGciOiJIUzUxMiJ9.eyJ0eXAiOiJKV1QiLCJyb2xlTmFtZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwidXNlcm5hbWUiOiI1MCIsImlhdCI6MTcwNzM2ODM1MywiZXhwIjoxNzA3NDU0NzUzfQ.A8JYHX1TBXXSPYo9EjFN91V65wS1Bxl--7g8fWAo0aPigOf94JrYAUCUi9Qs7fS8Ej2WZ4BxRv3Yc4R23QbNbA")
    private String refreshToken;

    public static UserTokenRefreshGenerationResponseDTO createUserTokenRefreshGenerationResponseDTO(String accessToken, String refreshToken) {
        return UserTokenRefreshGenerationResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
