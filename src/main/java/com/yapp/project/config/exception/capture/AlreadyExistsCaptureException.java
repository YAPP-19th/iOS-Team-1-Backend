package com.yapp.project.config.exception.capture;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.CaptureContent;
import lombok.Getter;

@Getter
public class AlreadyExistsCaptureException extends RuntimeException {
    private final StatusEnum status;
    public AlreadyExistsCaptureException(){
        super(CaptureContent.CAPTURE_ALREADY_FINISH);
        this.status = StatusEnum.CAPTURE_BAD_REQUEST;
    }
}
