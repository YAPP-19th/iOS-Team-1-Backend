package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import lombok.Getter;

@Getter
public class EmailDuplicateException extends RuntimeException{
    private final StatusEnum status;
    public EmailDuplicateException(){
        super(AccountContent.EMAIL_DUPLICATE);
        this.status = StatusEnum.EMAIL_BAD_REQUEST;
    }
}
