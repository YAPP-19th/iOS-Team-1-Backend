package com.yapp.project.config.exception.routine;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{
    private final StatusEnum status;
    public BadRequestException(String message, StatusEnum status){
        super(message);
        this.status = status;
    }
}