package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class NotValidateException extends IllegalArgumentException{
    private final StatusEnum status;
    public NotValidateException(String message, StatusEnum status){
        super(message);
        this.status = status;
    }
}
