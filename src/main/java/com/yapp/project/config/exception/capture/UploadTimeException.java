package com.yapp.project.config.exception.capture;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.CaptureContent;
import lombok.Getter;

import java.io.IOException;

@Getter
public class UploadTimeException extends RuntimeException {
    private final StatusEnum status;
    public UploadTimeException(){
        super(CaptureContent.CAPTURE_NOT_UPLOAD_TIME);
        this.status = StatusEnum.CAPTURE_BAD_REQUEST;
    }
}
