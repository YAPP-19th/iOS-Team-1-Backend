package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import lombok.Getter;

@Getter
public class EmailInvalidException extends IllegalArgumentException{
    private final StatusEnum status;
    public EmailInvalidException(){
        super(AccountContent.NOT_VALIDATION_EMAIL);
        this.status = StatusEnum.EMAIL_BAD_REQUEST;
    }
}
