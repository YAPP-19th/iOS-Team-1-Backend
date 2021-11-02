package com.yapp.project.config.exception;

import com.yapp.project.aux.alert.AlertService;
import com.yapp.project.aux.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final AlertService alertService;

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Message> handle(IOException e){
        final Message message = Message.of(e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
