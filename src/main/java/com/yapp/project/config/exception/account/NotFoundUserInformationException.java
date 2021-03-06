package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import lombok.Getter;

@Getter
public class NotFoundUserInformationException extends RuntimeException{
    private final StatusEnum status;
    public NotFoundUserInformationException(){
        super(AccountContent.NOT_FOUND_USER_INFORMATION);
        this.status = StatusEnum.USER_NOT_FOUND;
    }
}
