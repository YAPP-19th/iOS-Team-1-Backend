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
public class MissionScheduler {
    private final Job missionJob;
    private final JobLauncher jobLauncher;
    private final AlertService alertService;

    public MissionScheduler(JobLauncher jobLauncher, @Qualifier("missionJob") Job missionJob, AlertService alertService){
        this.missionJob = missionJob;
        this.jobLauncher = jobLauncher;
        this.alertService = alertService;
    }

    @Scheduled(cron = "0 1 0 * * *", zone = "Asia/Seoul")
    public void missionBatchExecuteJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":arrow_forward:어제 수행한 날인데 수행하지 않은 사람들 실패한 횟수 증가하는 배치작업 시작합니다.");
        jobLauncher.run(missionJob, new JobParametersBuilder()
                .addString("missionUpdateDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH,
                ":ballot_box_with_check:어제 수행한 날인데 수행하지 않은 사람들 실패한 횟수 증가하는 배치작업 마쳤습니다.");
    }
}