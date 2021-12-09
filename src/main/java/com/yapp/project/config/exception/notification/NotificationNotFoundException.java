package com.yapp.project.config.exception.notification;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.MissionContent;
import com.yapp.project.aux.content.NotificationContent;
import lombok.Getter;

@Getter
public class NotificationNotFoundException extends RuntimeException{
    private final StatusEnum status;
    public NotificationNotFoundException(){
        super(NotificationContent.NOTIFICATION_NOT_FOUND);
        this.status = StatusEnum.BAD_REQUEST;
    }
}
