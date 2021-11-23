package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import lombok.Getter;

@Getter
public class PasswordInvalidException extends IllegalArgumentException{
    private final StatusEnum status;
    public PasswordInvalidException(){
        super(AccountContent.NOT_VALIDATION_PASSWORD);
        this.status = StatusEnum.PASSWORD_BAD_REQUEST;
    }
}
