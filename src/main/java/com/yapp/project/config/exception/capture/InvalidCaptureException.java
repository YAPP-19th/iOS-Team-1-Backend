package com.yapp.project.config.exception.capture;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.CaptureContent;
import lombok.Getter;

import java.io.IOException;

@Getter
public class InvalidCaptureException extends IOException {
    private final StatusEnum status;
    public InvalidCaptureException(){
        super(CaptureContent.CAPTURE_NOT_FOUND);
        this.status = StatusEnum.CAPTURE_BAD_REQUEST;
    }
}
