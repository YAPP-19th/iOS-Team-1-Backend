package com.yapp.project.batch.scheduler;

import com.yapp.project.aux.alert.AlertService;
import com.yapp.project.aux.alert.SlackChannel;
import com.yapp.project.aux.common.DateUtil;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.yapp.project.aux.content.ReportContent.*;

@Component
public class ReportScheduler {
    private final Job weekReportJob;
    private final Job monthReportJob;
    private final JobLauncher jobLauncher;
    private final AlertService alertService;

    public ReportScheduler(JobLauncher jobLauncher, @Qualifier("makeWeekReportJob") Job weekReportJob,
                           @Qualifier("makeMonthReportJob") Job monthReportJob, AlertService alertService){
        this.weekReportJob = weekReportJob;
        this.monthReportJob = monthReportJob;
        this.jobLauncher = jobLauncher;
        this.alertService = alertService;
    }

    @Scheduled(cron = "0 0 2 * * 3", zone = "Asia/Seoul")
    public void makeWeekReportJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH, WEEK_REPORT_CREATE_START);
        jobLauncher.run(weekReportJob, new JobParametersBuilder()
                .addString("makeWeekReportDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH, WEEK_REPORT_CREATE_END);
    }

    @Scheduled(cron = "0 0 3 ? * 3#1", zone = "Asia/Seoul")
    public void makeMonthReportJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH, MONTH_REPORT_CREATE_START);
        jobLauncher.run(monthReportJob, new JobParametersBuilder()
                .addString("makeMonthReportDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH, MONTH_REPORT_CREATE_END);
    }
}
