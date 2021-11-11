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

    @Scheduled(cron = "0 0 17 * * 3")
    public void makeWeekReportJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":arrow_forward:주 리포트 발급 배치작업 시작합니다.");
        jobLauncher.run(weekReportJob, new JobParametersBuilder()
                .addString("makeWeekReportDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":ballot_box_with_check:주 리포트 발급 배치작업 마쳤습니다.");
    }

    @Scheduled(cron = "0 0 18 1 * ?")
    public void makeMonthReportJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":arrow_forward:월 리포트 발급 배치작업 시작합니다.");
        jobLauncher.run(monthReportJob, new JobParametersBuilder()
                .addString("makeWeekReportDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":ballot_box_with_check:월 리포트 발급 배치작업 마쳤습니다.");
    }
}