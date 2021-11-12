package com.yapp.project.batch.job;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.fcm.FireBaseCloudMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class ReportNotificationConfig {
    private final AccountRepository accountRepository;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final FireBaseCloudMessageService fireBaseCloudMessageService;

    private final String TITLE = "리포트 발급 알림";
    private final String WEEK_BODY = "주 리포트가 발급되었습니다!";
    private final String MONTH_BODY = "월 리포트가 발급되었습니다!";

    @Bean(name = "weekReportNotificationJob")
    public Job weekReportNotificationJob() {
        return jobBuilderFactory.get("weekReportNotificationJob")
                .start(weekReportNotificationStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean(name = "monthReportNotificationJob")
    public Job monthReportNotificationJob() {
        return jobBuilderFactory.get("monthReportNotificationJob")
                .start(monthReportNotificationStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step weekReportNotificationStep() {
        return stepBuilderFactory.get("weekReportNotificationStep")
                .<Account, Account> chunk(5)
                .reader(reportNotificationReader())
                .processor(weekReportNotificationProcessor())
                .writer(reportNotificationWriter())
                .build();
    }
    @Bean
    public Step monthReportNotificationStep() {
        return stepBuilderFactory.get("monthReportNotificationStep")
                .<Account, Account> chunk(5)
                .reader(reportNotificationReader())
                .processor(monthReportNotificationProcessor())
                .writer(reportNotificationWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Account> reportNotificationReader() {
        List<Account> accounts = accountRepository.findAll();
        return new ListItemReader<>(accounts);
    }

    public ItemProcessor<Account, Account> weekReportNotificationProcessor() {
        ItemProcessor<Account, Account> itemProcessor = new ItemProcessor<>() {
            @Override
            public Account process(Account account) throws Exception {
                fireBaseCloudMessageService.sendMessageTo(account.getFcmToken(), TITLE, WEEK_BODY);
                return null;
            }
        };
        return itemProcessor;
    }

    public ItemProcessor<Account, Account> monthReportNotificationProcessor() {
        ItemProcessor<Account, Account> itemProcessor = new ItemProcessor<>() {
            @Override
            public Account process(Account account) throws Exception {
                fireBaseCloudMessageService.sendMessageTo(account.getFcmToken(), TITLE, MONTH_BODY);
                return null;
            }
        };
        return itemProcessor;
    }

    public ItemWriter<Account> reportNotificationWriter() {
        ItemWriter<Account> itemWriter = new ItemWriter<>() {
            @Override
            public void write(List<? extends Account> items) throws Exception {
                log.info("Complete One Chunk");
            }
        };
        return itemWriter;
    }
}