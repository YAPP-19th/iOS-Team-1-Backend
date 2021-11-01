package com.yapp.project.config.exception.report;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.ReportContent;
import lombok.Getter;

@Getter
public class WeekReportNotFoundMonthException extends RuntimeException{
    private final StatusEnum status;
    public WeekReportNotFoundMonthException(){
        super(ReportContent.WEEK_REPORT_NOT_FOUND);
        this.status = StatusEnum.WEEK_REPORT_NOT_FOUND;
    }
}
