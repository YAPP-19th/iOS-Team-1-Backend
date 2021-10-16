package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class AlreadyLogoutException extends RuntimeException{
    private final StatusEnum status;
    public AlreadyLogoutException(){
        super(AccountContent.LOGOUT_USER);
        this.status = StatusEnum.BAD_REQUEST;
    }
}
