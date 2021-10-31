package com.yapp.project.config.exception.report;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.ReportContent;
import lombok.Getter;

@Getter
public class MonthReportNotFoundMonthException extends RuntimeException{
    private final StatusEnum status;
    public MonthReportNotFoundMonthException(){
        super(ReportContent.MONTH_REPORT_NOT_FOUND);
        this.status = StatusEnum.MONTH_REPORT_NOT_FOUND;
    }
}
