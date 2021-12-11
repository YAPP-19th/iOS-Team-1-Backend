package com.yapp.project.batch.scheduler;

import com.yapp.project.aux.common.DateUtil;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrganizationScheduler {
    private final Job job;
    private final JobLauncher jobLauncher;

    public OrganizationScheduler(JobLauncher jobLauncher, @Qualifier("organizationJob") Job organizationJob){
        this.job = organizationJob;
        this.jobLauncher = jobLauncher;
    }

    @Scheduled(cron = "0 1 * * * *")
    public void groupBatchExecuteJob() throws JobExecutionException{
        jobLauncher.run(job, new JobParametersBuilder()
                            .addString("organizationUpdateRate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                            .toJobParameters());
    }

}
