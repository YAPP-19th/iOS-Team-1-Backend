package com.yapp.project.config.exception.routine;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class BadRequestRoutineException extends RuntimeException{
    private final StatusEnum status;
    public BadRequestRoutineException(){
        super(RoutineContent.BAD_REQUEST_GET_ROUTINE_ID);
        this.status = StatusEnum.ROUTINE_BAD_REQUEST;
    }
}