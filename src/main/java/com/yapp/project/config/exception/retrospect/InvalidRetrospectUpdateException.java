package com.yapp.project.config.exception.retrospect;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class InvalidRetrospectUpdateException extends RuntimeException {
    private final StatusEnum status;
    public InvalidRetrospectUpdateException(){
        super(RetrospectContent.INVALID_UPDATE_RETROSPECT);
        this.status = StatusEnum.RETROSPECT_BAD_REQUEST;
    }
}
