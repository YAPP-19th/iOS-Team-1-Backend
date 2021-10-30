package com.yapp.project.config.exception.report;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.ReportContent;
import lombok.Getter;

@Getter
public class AlreadyWeekReportFoundException extends RuntimeException{
    private final StatusEnum status;
    public AlreadyWeekReportFoundException(){
        super(ReportContent.WEEK_REPORT_IS_EXIST);
        this.status = StatusEnum.WEEK_REPORT_BAD_REQUEST;
    }
}
