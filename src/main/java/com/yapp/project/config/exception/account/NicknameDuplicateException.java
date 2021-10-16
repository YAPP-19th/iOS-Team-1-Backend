package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class NicknameDuplicateException extends RuntimeException{
    private final StatusEnum status;
    public NicknameDuplicateException(){
        super(AccountContent.NICKNAME_DUPLICATE);
        this.status = StatusEnum.NICKNAME_BAD_REQUEST;
    }
}
