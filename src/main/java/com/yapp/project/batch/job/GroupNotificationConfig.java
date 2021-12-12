package com.yapp.project.batch.job;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.alert.AlertService;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.fcm.FireBaseCloudMessageService;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.service.MissionService;
import com.yapp.project.organization.domain.Category;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class GroupNotificationConfig {
    private final MissionService missionService;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final FireBaseCloudMessageService fireBaseCloudMessageService;
    private final AlertService alertService;

    private static final String START_ALARM_TITLE = "그룹안내";
    private static final String START_ALARM_CONTENT_PREFIX = "미닝과 함께 ";
    private static final String START_ALARM_CONTENT_SUFFIX = " 그룹 활동 달려봐요!";
    private static final String FINISH_CONTENT = "의미 있는 아침을 위해 노력한 당신! 그동안 수고 많으셨어요. 🔅";
    private static final String FINISH_TITLE = "그룹이 종료되었습니다.";

    @Bean(name = "missionFinishNotificationJob")
    public Job missionFinishNotificationJob() {
        return jobBuilderFactory.get("missionFinishNotificationJob")
                .start(missionFinishNotificationStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean(name = "missionWakeUpJob")
    public Job missionFourClockWakeUpJob(){
        return jobBuilderFactory.get("missionWakeUpJob")
                .start(missionWakeUpNotificationStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step missionWakeUpNotificationStep() {
        return stepBuilderFactory.get("missionWakeJobStep")
                .<Mission, Account> chunk(5)
                .reader(missionWakeUpListItemReader())
                .processor(missionWakeUpNotificationProcessor())
                .writer(missionWakeUpWriter())
                .build();
    }
    @Bean
    @StepScope
    public ListItemReader<Mission> missionWakeUpListItemReader(){
        LocalDateTime now = DateUtil.KST_LOCAL_DATETIME_NOW();
        LocalDate date = now.toLocalDate();
        LocalTime time = null;
        int hour = now.getHour();
        int min = now.getMinute();
        if(min>=40){
            time = LocalTime.of(hour+1,0);
        }else{
            time = LocalTime.of(hour,0);
        }
        now = LocalDateTime.of(date,time);
        List<Mission> missions = missionService.getWakeUpClockMission(now);
        return new ListItemReader<>(missions);
    }

    public ItemProcessor<Mission, Account> missionWakeUpNotificationProcessor(){
        return item -> {
            Account account = item.getAccount();
            Category category = item.getOrganization().getCategory();
            fireBaseCloudMessageService.
                    sendMessageTo(account.getFcmToken(),
                            START_ALARM_TITLE,
                            START_ALARM_CONTENT_PREFIX + category + START_ALARM_CONTENT_SUFFIX);
            return null;
        };
    }

    public ItemWriter<Account> missionWakeUpWriter(){
        return new ItemWriter<Account>() {
            @Override
            public void write(@NotNull List<? extends Account> items) throws Exception {
                alertService.slackSendMessage("미션 시작할 사람들에게 메세지 보내기 완료");
            }
        };
    }

    @Bean
    public Step missionFinishNotificationStep() {
        return stepBuilderFactory.get("missionFinishNotificationStep")
                .<Mission, Account> chunk(5)
                .reader(missionNotificationReader())
                .processor(missionFinishNotificationProcessor())
                .writer(missionFinishNotificationWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Mission> missionNotificationReader() {
        List<Mission> missions = missionService.checkLastDayMission();
        return new ListItemReader<>(missions);
    }

    public ItemProcessor<Mission, Account> missionFinishNotificationProcessor() {
        return item -> {
            Account account = item.getAccount();
            String category = item.getOrganization().getTitle();
            fireBaseCloudMessageService.
                    sendMessageTo(account.getFcmToken(), category+ FINISH_TITLE, FINISH_CONTENT);
            return null;
        };
    }



    public ItemWriter<Account> missionFinishNotificationWriter(){
        return items -> alertService.slackSendMessage("미션 종료된 사람들에게 메세지 보내기 완료");
    }



}
