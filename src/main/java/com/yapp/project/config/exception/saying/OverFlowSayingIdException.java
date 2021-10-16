package com.yapp.project.config.exception.saying;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class OverFlowSayingIdException extends RuntimeException{
    private final StatusEnum status;
    public OverFlowSayingIdException(){
        super(SayingContent.OVER_SIZE_ID_NUMBER);
        this.status = StatusEnum.SAYING_NOT_FOUND;
    }
}
