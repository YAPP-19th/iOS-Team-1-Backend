package com.yapp.project.config.exception.capture;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.CaptureContent;
import lombok.Getter;

@Getter
public class NotTodayCaptureException extends RuntimeException {
    private final StatusEnum status;
    public NotTodayCaptureException(){
        super(CaptureContent.CAPTURE_NOT_UPLOAD_DAY);
        this.status = StatusEnum.CAPTURE_BAD_REQUEST;
    }
}
