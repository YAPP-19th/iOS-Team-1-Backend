package com.yapp.project.config.exception.saying;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class AlreadyFoundException extends RuntimeException{
    private final StatusEnum status;
    public AlreadyFoundException(){
        super(SayingContent.ALREADY_FOUND_SAYING_RECORD);
        this.status = StatusEnum.SAYING_BAD_REQUEST;
    }
}
