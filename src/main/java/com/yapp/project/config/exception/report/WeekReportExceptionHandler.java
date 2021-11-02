package com.yapp.project.config.exception.report;

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
public class WeekReportExceptionHandler {
    private final AlertService alertService;

    @ExceptionHandler(AlreadyWeekReportFoundException.class)
    public ResponseEntity<Message> handle(AlreadyWeekReportFoundException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MonthReportNotFoundMonthException.class)
    public ResponseEntity<Message> handle(MonthReportNotFoundMonthException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoutineStartDayBadRequestException.class)
    public ResponseEntity<Message> handle(RoutineStartDayBadRequestException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WeekReportNotFoundMonthException.class)
    public ResponseEntity<Message> handle(WeekReportNotFoundMonthException e){
        final Message message = Message.of(e.getStatus() ,e.getMessage());
        alertService.sentryWithSlackMessage(e);
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }

}
