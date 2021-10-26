package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import lombok.Getter;

@Getter
public class NotEqualAuthenticationNumberException extends RuntimeException{
    private final StatusEnum status;
    public NotEqualAuthenticationNumberException(){
        super(AccountContent.NOT_EQUAL_NUMBER);
        this.status = StatusEnum.AUTHENTICATION_NUMBER_BAD_REQUEST;
    }
}
