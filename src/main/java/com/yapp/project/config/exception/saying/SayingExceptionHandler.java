package com.yapp.project.config.exception.saying;

import com.yapp.project.aux.AlertService;
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
public class SayingExceptionHandler {
    private final AlertService alertService;

    @ExceptionHandler(AlreadyFoundException.class)
    public ResponseEntity<Message> handle(AlreadyFoundException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OverFlowSayingIdException.class)
    public ResponseEntity<Message> handle(OverFlowSayingIdException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }
}
