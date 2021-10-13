package com.yapp.project.config.exception.saying;

import com.yapp.project.aux.StatusEnum;

public class NotFoundSayingRecordException extends RuntimeException{
    private final StatusEnum status;
    public NotFoundSayingRecordException(String message, StatusEnum status){
        super(message);
        this.status = status;
    }
}
