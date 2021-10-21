package com.yapp.project.config.exception.retrospect;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.routine.RoutineContent;
import lombok.Getter;

@Getter
public class BadRequestRetrospectException extends RuntimeException{
    private final StatusEnum status;
    public BadRequestRetrospectException(){
        super(RetrospectContent.BAD_REQUEST_RETROSPECT);
        this.status = StatusEnum.RETROSPECT_BAD_REQUEST;
    }
}