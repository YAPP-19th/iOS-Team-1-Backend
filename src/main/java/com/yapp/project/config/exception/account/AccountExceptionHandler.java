package com.yapp.project.config.exception.account;

import com.yapp.project.aux.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AccountExceptionHandler {
    @ExceptionHandler(AlreadyLogoutException.class)
    public ResponseEntity<Message> handle(AlreadyLogoutException e){
        final Message message = Message.of(e.getStatus(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<Message> handle(EmailDuplicateException e){
        final Message message = Message.of(e.getStatus(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NicknameDuplicateException.class)
    public ResponseEntity<Message> handle(NicknameDuplicateException e){
        final Message message = Message.of(e.getStatus(), e.getMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NotFoundUserInformationException.class)
    public ResponseEntity<Message> handle(NotFoundUserInformationException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<Message> handle(PasswordInvalidException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<Message> handle(RefreshTokenInvalidException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<Message> handle(TokenInvalidException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }
}
