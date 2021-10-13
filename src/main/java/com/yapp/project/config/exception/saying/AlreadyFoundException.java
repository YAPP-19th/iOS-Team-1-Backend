package com.yapp.project.config.exception.saying;

import com.yapp.project.aux.StatusEnum;

public class AlreadyFoundException extends RuntimeException{
    private final StatusEnum status;
    public AlreadyFoundException(String message, StatusEnum status){
        super(message);
        this.status = status;
    }
}
