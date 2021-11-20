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
public class MissionNotificationScheduler {
    private final Job job;
    private final Job job2;
    private final JobLauncher jobLauncher;
    private final AlertService alertService;

    public MissionNotificationScheduler(JobLauncher jobLauncher,
                                        @Qualifier("missionFinishNotificationJob") Job finishJob,
                                        @Qualifier("missionWakeUpJob") Job startJob,
                                        AlertService alertService){
        this.job = finishJob;
        this.job2 = startJob;
        this.jobLauncher = jobLauncher;
        this.alertService = alertService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void missionFinishBatchExecuteJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH,":arrow_forward:종료된 미션에 대한 배치작업 시작합니다.");
        jobLauncher.run(job, new JobParametersBuilder()
                            .addString("missionFinishBatch", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                            .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH,":ballot_box_with_check:종료된 미션에 대한 배치작업 끝났습니다.");
    }

    @Scheduled(cron = "0 50 18,19,20,21,22 * * *")
    public void missionFourWakeUpBatchExecuteJob() throws JobExecutionException{
        alertService.slackSendMessage(SlackChannel.BATCH,":arrow_forward:기상 미션에 대한 배치작업 시작합니다.");
        jobLauncher.run(job2, new JobParametersBuilder()
                .addString("missionFourClockWakeUp", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters());
        alertService.slackSendMessage(SlackChannel.BATCH,":ballot_box_with_check:기상 미션에 대한 배치작업 끝났습니다.");
    }





}
