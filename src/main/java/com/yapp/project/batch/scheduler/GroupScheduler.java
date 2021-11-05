package com.yapp.project.batch.scheduler;

import com.yapp.project.aux.alert.AlertService;
import com.yapp.project.aux.alert.SlackChannel;
import com.yapp.project.aux.common.DateUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GroupScheduler {
    private final Job job;
    private final JobLauncher jobLauncher;
    private final AlertService alertService;

    public GroupScheduler(JobLauncher jobLauncher, @Qualifier("groupJob") Job groupJob, AlertService alertService){
        this.job = groupJob;
        this.jobLauncher = jobLauncher;
        this.alertService = alertService;
    }

    @Scheduled(cron = "0 50 * * * *")
    public void groupBatchExecuteJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH,":arrow_forward:배치작업 시작합니다.");
        jobLauncher.run(job, new JobParametersBuilder()
                            .addString("groupUpdateDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                            .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH,":black_square_for_stop:배치작업 끝났습니다.");
    }

}
