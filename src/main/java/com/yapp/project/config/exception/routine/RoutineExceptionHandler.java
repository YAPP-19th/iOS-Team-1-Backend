package com.yapp.project.config.exception.routine;

import com.yapp.project.aux.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class RoutineExceptionHandler {

    @ExceptionHandler(NotFoundRoutineException.class)
    public ResponseEntity<Message> handle(NotFoundRoutineException e) {
        final Message message = Message.of(e.getStatus(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestRoutineException.class)
    public ResponseEntity<Message> handle(BadRequestRoutineException e) {
        final Message message = Message.of(e.getStatus(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}