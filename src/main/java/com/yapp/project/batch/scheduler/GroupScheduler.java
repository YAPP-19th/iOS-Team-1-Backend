package com.yapp.project.batch.scheduler;

import com.yapp.project.aux.AlertService;
import com.yapp.project.aux.common.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AlertService alertService;

    public GroupScheduler(JobLauncher jobLauncher, @Qualifier("groupJob") Job groupJob){
        this.job = groupJob;
        this.jobLauncher = jobLauncher;
    }

    @Scheduled(cron = "0 30 0 * * *")
    public void executeJob() throws JobExecutionException{
        alertService.slackSendMessage(":arrow_forward:배치작업 시작합니다.");
        jobLauncher.run(job, new JobParametersBuilder()
                            .addString("groupUpdateDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                            .toJobParameters());
        alertService.slackSendMessage(":black_square_for_stop:배치작업 끝났습니다.");
    }

}
