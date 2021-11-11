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

    @Scheduled(cron = "0 0 22 * * 3")
    public void weekReportNotificationJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":arrow_forward:주 리포트 알림 푸시 시작합니다.");
        jobLauncher.run(weekNotificationJob, new JobParametersBuilder()
                .addString("weekReportNotificationDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":ballot_box_with_check:주 리포트 알림 푸시 마쳤습니다.");
    }

    @Scheduled(cron = "0 0 23 1 * ?")
    public void monthReportNotificationJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":arrow_forward:월 리포트 알림 푸시 시작합니다.");
        jobLauncher.run(monthNotificationJob, new JobParametersBuilder()
                .addString("monthReportNotificationDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":ballot_box_with_check:월 리포트 알림 푸시 마쳤습니다.");
    }
}