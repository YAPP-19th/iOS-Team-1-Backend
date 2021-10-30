package com.yapp.project.batch.scheduler;

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

    public GroupScheduler(JobLauncher jobLauncher, @Qualifier("groupJob") Job groupJob){
        this.job = groupJob;
        this.jobLauncher = jobLauncher;
    }

    @Scheduled(cron = "0 30 0 * * *")
    public void executeJob() throws JobExecutionException{
        jobLauncher.run(job, new JobParametersBuilder()
                            .addString("groupUpdateDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                            .toJobParameters());
    }

}
