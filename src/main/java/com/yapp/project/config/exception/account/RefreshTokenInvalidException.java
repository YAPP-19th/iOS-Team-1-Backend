package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import lombok.Getter;

@Getter
public class RefreshTokenInvalidException extends RuntimeException{
    private final StatusEnum status;
    public RefreshTokenInvalidException(){
        super(AccountContent.REFRESH_TOKEN_INVALID);
        this.status = StatusEnum.REFRESH_BAD_REQUEST;
    }
}
