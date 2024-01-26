package com.d101.frientree.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash(value = "refreshToken", timeToLive = 3600 * 24)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {

    @Id
    private String refreshToken;

    private String username;

    private String expiryDate;
}
