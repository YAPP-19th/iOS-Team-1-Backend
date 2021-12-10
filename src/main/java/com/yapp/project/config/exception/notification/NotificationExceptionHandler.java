package com.yapp.project.config.exception.notification;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.alert.AlertService;
import com.yapp.project.config.exception.mission.AlreadyMissionExistException;
import com.yapp.project.config.exception.mission.MissionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class NotificationExceptionHandler {
    private final AlertService alertService;

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<Message> handle(NotificationNotFoundException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }

}
