package com.yapp.project.config.exception.account;

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
public class AccountExceptionHandler {

    private final AlertService alertService;

    @ExceptionHandler(AlreadyLogoutException.class)
    public ResponseEntity<Message> handle(AlreadyLogoutException e){
        final Message message = Message.of(e.getStatus(), e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<Message> handle(EmailDuplicateException e){
        final Message message = Message.of(e.getStatus(), e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailInvalidException.class)
    public ResponseEntity<Message> handle(EmailInvalidException e){
        final Message message = Message.of(e.getStatus(), e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NicknameLengthOverException.class)
    public ResponseEntity<Message> handle(NicknameLengthOverException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundUserInformationException.class)
    public ResponseEntity<Message> handle(NotFoundUserInformationException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<Message> handle(PasswordInvalidException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<Message> handle(RefreshTokenInvalidException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<Message> handle(TokenInvalidException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEqualAuthenticationNumberException.class)
    public ResponseEntity<Message> handle(NotEqualAuthenticationNumberException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(SocialTokenInvalidException.class)
    public ResponseEntity<Message> handle(SocialTokenInvalidException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

}
