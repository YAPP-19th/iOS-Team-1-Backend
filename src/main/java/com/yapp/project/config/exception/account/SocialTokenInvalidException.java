package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import lombok.Getter;

@Getter
public class SocialTokenInvalidException extends RuntimeException{
    private final StatusEnum status;
    public SocialTokenInvalidException(){
        super(AccountContent.SOCIAL_TOKEN_INVALID);
        this.status = StatusEnum.TOKEN_BAD_REQUEST;
    }
}
