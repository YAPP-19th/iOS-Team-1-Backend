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
    private final Job missionJob;
    private final JobLauncher jobLauncher;
    private final AlertService alertService;

    public ReportScheduler(JobLauncher jobLauncher, @Qualifier("makeWeekReportJob") Job weekReportJob, AlertService alertService){
        this.missionJob = weekReportJob;
        this.jobLauncher = jobLauncher;
        this.alertService = alertService;
    }

    @Scheduled(cron = "0 0 17 * * 3")
    public void missionBatchExecuteJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":arrow_forward:주 리포트 발급 배치작업 시작합니다.");
        jobLauncher.run(missionJob, new JobParametersBuilder()
                .addString("makeWeekReportDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":ballot_box_with_check:주 리포트 발급 배치작업 마쳤습니다.");
    }
}