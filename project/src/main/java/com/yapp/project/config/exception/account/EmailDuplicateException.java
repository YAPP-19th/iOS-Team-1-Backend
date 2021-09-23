package com.yapp.project.config.exception.account;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class EmailDuplicateException extends RuntimeException{
    private final StatusEnum status;
    public EmailDuplicateException(String message, StatusEnum status){
        super(message);
        this.status = status;
    }
}
