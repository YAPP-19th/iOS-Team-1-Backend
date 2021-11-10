package com.yapp.project.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class TestBatchLegacyConfig {
    @Bean
    public JobLauncherTestUtils jobLauncherTestUtilsForGroup(){
        return new JobLauncherTestUtils(){
            @Override
            @Autowired
            public void setJob(@Qualifier("groupJob") Job job) {
                super.setJob(job);
            }
        };
    }

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtilsForMission(){
        return new JobLauncherTestUtils(){
            @Override
            @Autowired
            public void setJob(@Qualifier("groupJob") Job job) {
                super.setJob(job);
            }
        };
    }
}
