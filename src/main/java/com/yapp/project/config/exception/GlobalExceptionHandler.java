package com.yapp.project.config.exception;

import com.yapp.project.aux.Message;
import com.yapp.project.config.exception.account.EmailDuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<Message> handle(EmailDuplicateException e){
        final Message message = Message.of(e.getStatus(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
