package com.yapp.project.config.exception.routine;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class BadRequestRoutineException extends RuntimeException{
    private final StatusEnum status;
    public BadRequestRoutineException(){
        super(RoutineContent.BAD_REQUEST_CREATE_ROUTINE_DATA);
        this.status = StatusEnum.ROUTINE_BAD_REQUEST;
    }
}