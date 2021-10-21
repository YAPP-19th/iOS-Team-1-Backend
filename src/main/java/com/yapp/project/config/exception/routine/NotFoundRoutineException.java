package com.yapp.project.config.exception.routine;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class NotFoundRoutineException extends RuntimeException{
    private final StatusEnum status;
    public NotFoundRoutineException(){
        super(RoutineContent.NOT_FOUND_ROUTINE);
        this.status = StatusEnum.ROUTINE_NOT_FOUND;
    }
}