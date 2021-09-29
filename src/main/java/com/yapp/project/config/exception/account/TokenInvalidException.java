package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class TokenInvalidException extends RuntimeException{
    private final StatusEnum status;
    public TokenInvalidException(String message, StatusEnum status){
        super(message);
        this.status = status;
    }
}
