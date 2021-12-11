package com.yapp.project.config.exception.notice;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.NoticeContent;
import lombok.Getter;

@Getter
public class NoticeNotFoundException extends IllegalArgumentException{
    private final StatusEnum status;
    public NoticeNotFoundException(){
        super(NoticeContent.NOTICE_NOT_FOUND);
        this.status = StatusEnum.NOTICE_NOT_FOUND;
    }
}
