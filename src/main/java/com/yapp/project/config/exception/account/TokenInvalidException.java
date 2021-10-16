package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class TokenInvalidException extends RuntimeException{
    private final StatusEnum status;
    public TokenInvalidException(){
        super(AccountContent.TOKEN_NOT_EQUAL_USER_INFORMATION);
        this.status = StatusEnum.TOKEN_BAD_REQUEST;
    }
}
