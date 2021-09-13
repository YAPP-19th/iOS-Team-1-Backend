package com.yapp.project.account.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    private String key;
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
