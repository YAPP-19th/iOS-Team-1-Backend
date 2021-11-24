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

    private final String WEEK_TITLE = "주간 리포트가 도착했습니다 \uD83D\uDC8C";
    private final String MONTH_TITLE = "월간 리포트가 도착했습니다 \uD83D\uDC8C";
    private final String WEEK_BODY = "님의 한 주간의 발자취를 확인해보세요! \uD83D\uDC3E";
    private final String MONTH_BODY = "님의 한 달간의 발자취를 확인해보세요! \uD83D\uDC3E";

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
        List<Account> accounts = accountRepository.findAllByIsAlarmIsTrue();
        return new ListItemReader<>(accounts);
    }

    public ItemProcessor<Account, Account> weekReportNotificationProcessor() {
        ItemProcessor<Account, Account> itemProcessor = new ItemProcessor<>() {
            @Override
            public Account process(Account account) throws Exception {
                fireBaseCloudMessageService.
                        sendMessageTo(account.getFcmToken(), WEEK_TITLE, account.getNickname() + WEEK_BODY);
                return null;
            }
        };
        return itemProcessor;
    }

    public ItemProcessor<Account, Account> monthReportNotificationProcessor() {
        ItemProcessor<Account, Account> itemProcessor = new ItemProcessor<>() {
            @Override
            public Account process(Account account) throws Exception {
                fireBaseCloudMessageService.
                        sendMessageTo(account.getFcmToken(), MONTH_TITLE, account.getNickname() + MONTH_BODY);
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