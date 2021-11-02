package com.yapp.project.config.exception.mission;

import com.yapp.project.aux.alert.AlertService;
import com.yapp.project.aux.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class MissionExceptionHandler {
    private final AlertService alertService;

    @ExceptionHandler(AlreadyMissionExistException.class)
    public ResponseEntity<Message> handle(AlreadyMissionExistException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissionNotFoundException.class)
    public ResponseEntity<Message> handle(MissionNotFoundException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }

}
