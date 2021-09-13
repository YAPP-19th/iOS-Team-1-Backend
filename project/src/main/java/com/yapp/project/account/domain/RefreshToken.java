package com.yapp.project.account.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@NoArgsConstructor
@RedisHash("refreshToken")
public class RefreshToken {

    @Id
    private String key;
    @Indexed
    private String value;

    public RefreshToken updateValue(String value){
        this.value = value;
        return this;
    }

    @Builder
    public RefreshToken(String key, String value){
        this.key = key;
        this.value = value;
    }
}
