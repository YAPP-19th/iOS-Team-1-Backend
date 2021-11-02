package com.yapp.project.config.exception.routine;

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
public class RoutineExceptionHandler {
    private final AlertService alertService;

    @ExceptionHandler(NotFoundRoutineException.class)
    public ResponseEntity<Message> handle(NotFoundRoutineException e) {
        final Message message = Message.of(e.getStatus(), e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestRoutineException.class)
    public ResponseEntity<Message> handle(BadRequestRoutineException e) {
        final Message message = Message.of(e.getStatus(), e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}