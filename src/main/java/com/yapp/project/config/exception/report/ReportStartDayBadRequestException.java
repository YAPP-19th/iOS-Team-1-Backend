package com.yapp.project.config.exception.report;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.ReportContent;
import lombok.Getter;

@Getter
public class ReportStartDayBadRequestException extends RuntimeException{
    private final StatusEnum status;
    public ReportStartDayBadRequestException(){
        super(ReportContent.START_DATE_IS_NOT_MON);
        this.status = StatusEnum.DAY_ROUTINE_RATE_BAD_REQUEST;
    }
}
