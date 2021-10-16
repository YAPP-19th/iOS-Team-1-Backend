package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class RefreshTokenInvalidException extends RuntimeException{
    private final StatusEnum status;
    public RefreshTokenInvalidException(){
        super(AccountContent.REFRESH_TOKEN_INVALID);
        this.status = StatusEnum.BAD_REQUEST;
    }
}
