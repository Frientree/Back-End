package com.d101.frientree.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserFcmTokenUpdateRequest {
    @Schema(description = "fcm 토큰", example = "ezLZNj7flyY:APA91bGzOJ6vAaRnKZ")
    private String fcmToken;
}
