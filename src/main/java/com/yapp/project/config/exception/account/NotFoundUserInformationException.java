package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class NotFoundUserInformationException extends RuntimeException{
    private final StatusEnum status;
    public NotFoundUserInformationException(String message, StatusEnum status){
        super(message);
        this.status = status;
    }
}
