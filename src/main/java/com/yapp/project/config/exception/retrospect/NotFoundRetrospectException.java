package com.yapp.project.config.exception.retrospect;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.routine.RoutineContent;
import lombok.Getter;

import static com.yapp.project.config.exception.retrospect.RetrospectContent.NOT_FOUND_RETROSPECT;

@Getter
public class NotFoundRetrospectException extends RuntimeException{
    private final StatusEnum status;
    public NotFoundRetrospectException(){
        super(NOT_FOUND_RETROSPECT);
        this.status = StatusEnum.RETROSPECT_NOT_FOUND;
    }
}