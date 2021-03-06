package com.yapp.project.batch.job;

import com.yapp.project.aux.alert.AlertService;
import com.yapp.project.aux.alert.SlackChannel;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.capture.domain.Capture;
import com.yapp.project.capture.domain.repository.CaptureRepository;
import com.yapp.project.capture.service.CaptureService;
import com.yapp.project.mission.domain.Cron;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;


@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class MissionConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MissionService missionService;
    private final CaptureService captureService;
    private final AlertService alertService;

    @Bean(name="missionJob")
    public Job countFailJob(){
        return jobBuilderFactory.get("missionFailure")
                .preventRestart()
                .start(countFailJobStep())
                .build();
    }

    @Bean
    public Step countFailJobStep(){
        return stepBuilderFactory.get("countFailJobStep")
                .<Mission, Mission> chunk(10)
                .reader(missionCountReader())
                .processor(updateMissionProcessor())
                .writer(missionItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Mission> missionCountReader(){
        log.info("?????? ??????/?????? ????????? ?????? ????????????.");
        List<Mission> missions = missionService.findAllByIsDeleteIsFalseAndIsFinishIsFalse();
        String guide = "??? ?????? ????????? "+missions.size()+"??? ?????????.";
        log.info(guide);
        alertService.slackSendMessage(SlackChannel.BATCH,guide);
        return new ListItemReader<>(missions);
    }

    public ItemProcessor<Mission,Mission> updateMissionProcessor(){
        return new ItemProcessor<Mission, Mission>() {
            @Override
            public Mission process(@NotNull Mission item) throws Exception {
                List<Capture> list = captureService.findAllByMission(item);
                Capture capture = list.get(list.size() -1);
                if (!capture.getCreatedAt().toLocalDate().isEqual(DateUtil.KST_LOCAL_DATE_YESTERDAY())) {
                    log.info("?????? ????????? ?????? ?????? ????????? ?????? ??????");
                    List<Cron> weeks = item.getWeeks();
                    String day = DateUtil.KST_LOCAL_DATE_YESTERDAY().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                    if (weeks.stream().anyMatch(cron -> cron.getWeek().getWeek().equals(day))) {
                        log.info("??? ??? ????????? ????????? ?????? ?????? ????????? ?????? ??????");
                        item.updateFailureCount();
                    }
                }
                return item;
            }
        };
    }

    public ItemWriter<Mission> missionItemWriter(){
        return new ItemWriter<Mission>() {
            @Override
            public void write(@NotNull List<? extends Mission> items) throws Exception {
                log.info("?????? ?????? ?????? ?????? ?????? ????????? 1?????? ??????");
                missionService.saveAllMissions((List<Mission>) items);
            }
        };
    }
}