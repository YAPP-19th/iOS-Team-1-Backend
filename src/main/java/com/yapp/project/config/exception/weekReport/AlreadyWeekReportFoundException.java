package com.yapp.project.config.exception.weekReport;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.SayingContent;
import com.yapp.project.aux.content.WeekReportContent;
import lombok.Getter;

@Getter
public class AlreadyWeekReportFoundException extends RuntimeException{
    private final StatusEnum status;
    public AlreadyWeekReportFoundException(){
        super(WeekReportContent.WEEK_REPORT_IS_EXIST);
        this.status = StatusEnum.WEEK_REPORT_BAD_REQUEST;
    }
}
