package com.d101.frientree.entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash(value = "userEmail", timeToLive = 600)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailCode {

    @Id
    private String userEmail;

    private String code;
}
