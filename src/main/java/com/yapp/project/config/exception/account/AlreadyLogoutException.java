package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import lombok.Getter;

@Getter
public class AlreadyLogoutException extends RuntimeException{
    private final StatusEnum status;
    public AlreadyLogoutException(){
        super(AccountContent.LOGOUT_USER);
        this.status = StatusEnum.REFRESH_TOKEN_NOT_IN_REDIS;
    }
}
