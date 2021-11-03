package com.yapp.project.config.exception.retrospect;

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
public class RetrospectExceptionHandler {
    private final AlertService alertService;

    @ExceptionHandler(BadRequestRetrospectException.class)
    public ResponseEntity<Message> handle(BadRequestRetrospectException e) {
        final Message message = Message.of(e.getStatus(), e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundRetrospectException.class)
    public ResponseEntity<Message> handle(NotFoundRetrospectException e) {
        final Message message = Message.of(e.getStatus(), e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRetrospectUpdateException.class)
    public ResponseEntity<Message> handle(InvalidRetrospectUpdateException e) {
        final Message message = Message.of(e.getStatus(), e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}