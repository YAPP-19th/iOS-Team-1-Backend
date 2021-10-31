package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import lombok.Getter;

@Getter
public class NicknameLengthOverException extends RuntimeException{
    private final StatusEnum status;
    public NicknameLengthOverException(){
        super(AccountContent.ACCOUNT_NICKNAME_LENGTH_LIMIT);
        this.status = StatusEnum.NICKNAME_BAD_REQUEST;
    }
}
