package com.yapp.project.config.exception.notice;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.alert.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class NoticeExceptionHandler {
    private final AlertService alertService;

    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<Message> handle(NoticeNotFoundException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
