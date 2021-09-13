package com.yapp.project.account.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@RequiredArgsConstructor
@RedisHash("refreshToken")
public class RefreshToken {

    @Id
    private String key;
    @Indexed
    private String value;
}
