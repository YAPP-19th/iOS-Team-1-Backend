package com.yapp.project.config.exception.saying;

import com.yapp.project.aux.StatusEnum;

public class OverSizeException extends RuntimeException{
    private final StatusEnum status;
    public OverSizeException(String message, StatusEnum status){
        super(message);
        this.status = status;
    }
}
