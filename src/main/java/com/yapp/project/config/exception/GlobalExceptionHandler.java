package com.yapp.project.config.exception;

import com.yapp.project.aux.Message;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Message> handle(IOException e){
        final Message message = Message.of(e.getMessage());
        Sentry.captureException(e);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
