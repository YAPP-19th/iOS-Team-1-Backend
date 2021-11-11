package com.yapp.project.batch.job;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.alert.AlertService;
import com.yapp.project.aux.alert.SlackChannel;
import com.yapp.project.report.service.ReportService;
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
public class ReportConfig {
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final AlertService alertService;
    private final AccountRepository accountRepository;
    private final ReportService reportService;

    @Bean(name = "makeWeekReportJob")
    public Job makeWeekReportJob() {
        return jobBuilderFactory.get("makeWeekReportJob")
                .start(makeWeekReportStep())
                .incrementer(new RunIdIncrementer())
                .build();

    }

    @Bean(name = "makeMonthReportJob")
    public Job makeMonthReportJob() {
        return jobBuilderFactory.get("makeMonthReportJob")
                .start(makeMonthReportStep())
                .incrementer(new RunIdIncrementer())
                .build();

    }

    @Bean
    public Step makeWeekReportStep() {
        return stepBuilderFactory.get("makeWeekReportStep")
                .<Account, Account> chunk(10)
                .reader(makeReportReader())
                .processor(makeWeekReportProcessor())
                .writer(makeReportWriter())
                .build();
    }

    @Bean
    public Step makeMonthReportStep() {
        return stepBuilderFactory.get("makeMonthReportStep")
                .<Account, Account> chunk(10)
                .reader(makeReportReader())
                .processor(makeMonthReportProcessor())
                .writer(makeReportWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Account> makeReportReader() {
        List<Account> accountList = accountRepository.findAll();
        alertService.slackSendMessage(SlackChannel.BATCH, "총 " + accountList.size() + "명의 리포트를 발급합니다.");
        return new ListItemReader<>(accountList);
    }

    public ItemProcessor<Account, Account> makeWeekReportProcessor() {
        ItemProcessor<Account, Account> itemProcessor = new ItemProcessor<>() {
            @Override
            public Account process(Account account) throws Exception {
                reportService.makeWeekReport(account);
                log.info("이메일: " + account.getEmail() + " 발급 완료");
                return null;
            }
        };
        return itemProcessor;
    }

    public ItemProcessor<Account, Account> makeMonthReportProcessor() {
        ItemProcessor<Account, Account> itemProcessor = new ItemProcessor<>() {
            @Override
            public Account process(Account account) throws Exception {
                reportService.makeMonthReport(account);
                log.info("이메일: " + account.getEmail() + " 발급 완료");
                return null;
            }
        };
        return itemProcessor;
    }


    public ItemWriter<Account> makeReportWriter() {
        ItemWriter<Account> itemWriter = new ItemWriter<>() {
            @Override
            public void write(List<? extends Account> items) throws Exception {
                log.info("Complete One Chunk");
            }
        };
        return itemWriter;
    }
}