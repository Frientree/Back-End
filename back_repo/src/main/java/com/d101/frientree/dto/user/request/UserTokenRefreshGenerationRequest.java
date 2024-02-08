package com.d101.frientree.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenRefreshGenerationRequest {
    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzUxMiJ9.eyJ0eXAiOiJKV1QiLCJyb2xlTmFtZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfVVNFUiJ9XSwidXNlcm5hbWUiOiI1MCIsImlhdCI6MTcwNzM1Njg5NywiZXhwIjoxNzA5OTQ4ODk3fQ.RYcx_t7RdDkR6FLK1U-xztNWmilH9sXWhD01c05XsabOAwZbeo0nktHCVy0OUX6Q_bdQh-_DkaHo4fEYDdVKQQ")
    private String refreshToken;
}
