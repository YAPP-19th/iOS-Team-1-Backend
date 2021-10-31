package com.yapp.project.config.exception.report;

import com.yapp.project.aux.Message;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class WeekReportExceptionHandler {
    @ExceptionHandler(AlreadyWeekReportFoundException.class)
    public ResponseEntity<Message> handle(AlreadyWeekReportFoundException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        Sentry.captureException(e);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MonthReportNotFoundMonthException.class)
    public ResponseEntity<Message> handle(MonthReportNotFoundMonthException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        Sentry.captureException(e);
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }

}
