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
public class ReportNotificationScheduler {
    private final Job weekNotificationJob;
    private final Job monthNotificationJob;
    private final JobLauncher jobLauncher;
    private final AlertService alertService;

    public ReportNotificationScheduler(JobLauncher jobLauncher, @Qualifier("weekReportNotificationJob") Job weekNotificationJob,
                                       @Qualifier("monthReportNotificationJob") Job monthNotificationJob, AlertService alertService){
        this.weekNotificationJob = weekNotificationJob;
        this.monthNotificationJob = monthNotificationJob;
        this.jobLauncher = jobLauncher;
        this.alertService = alertService;
    }

//    @Scheduled(cron = "0 16 2 * * 3")
    @Scheduled(cron = "0/5 * * * * *")
    public void weekReportNotificationJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH, WEEK_REPORT_PUSH_START);
        jobLauncher.run(weekNotificationJob, new JobParametersBuilder()
                .addString("weekReportNotificationDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH, WEEK_REPORT_PUSH_END);
    }

//    @Scheduled(cron = "0 20 2 ? * 3#1")
    @Scheduled(cron = "0/5 * * * * *")
    public void monthReportNotificationJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH, MONTH_REPORT_PUSH_START);
        jobLauncher.run(monthNotificationJob, new JobParametersBuilder()
                .addString("monthReportNotificationDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH, MONTH_REPORT_PUSH_END);
    }
}