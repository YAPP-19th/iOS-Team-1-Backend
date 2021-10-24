package com.yapp.project.config.exception.capture;

import com.yapp.project.aux.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CaptureExceptionHandler {
    @ExceptionHandler(InvalidCaptureException.class)
    public ResponseEntity<Message> handle(InvalidCaptureException e){
        final Message message = Message.of(e.getStatus(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsCaptureException.class)
    public ResponseEntity<Message> handle(AlreadyExistsCaptureException e){
        final Message message = Message.of(e.getStatus(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
