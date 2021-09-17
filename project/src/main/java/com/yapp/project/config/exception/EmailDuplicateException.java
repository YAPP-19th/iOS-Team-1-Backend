package com.yapp.project.config.exception;

import com.yapp.project.base.StatusEnum;
import lombok.Getter;

@Getter
public class EmailDuplicateException extends RuntimeException{
    private final StatusEnum status;
    public EmailDuplicateException(String message, StatusEnum status){
        super(message);
        this.status = status;
    }
}
