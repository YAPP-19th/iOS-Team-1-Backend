package com.yapp.project.batch.job;

import com.yapp.project.aux.alert.AlertService;
import com.yapp.project.aux.alert.SlackChannel;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.capture.domain.Capture;
import com.yapp.project.mission.domain.Cron;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final MissionRepository missionRepository;
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
        log.info("미션 성공/실패 횟수를 읽고 있습니다.");
        List<Mission> missions = missionRepository.findAllByIsDeleteIsFalse();
        String guide = "총 미션 갯수는 "+missions.size()+"개 입니다.";
        log.info(guide);
        alertService.slackSendMessage(SlackChannel.BATCH,guide);
        return new ListItemReader<>(missions);
    }

    public ItemProcessor<Mission,Mission> updateMissionProcessor(){
        return new ItemProcessor<Mission, Mission>() {
            @Override
            public Mission process(Mission item) throws Exception {
                List<Capture> list = item.getCaptures();
                Capture capture = list.get(list.size() -1);
                if (!capture.getCreatedAt().toLocalDate().isEqual(DateUtil.KST_LOCAL_DATE_YESTERDAY())) {
                    log.info("어제 인증샷 찍지 않은 사람들 검거 완료");
                    List<Cron> weeks = item.getWeeks();
                    String day = DateUtil.KST_LOCAL_DATE_YESTERDAY().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                    if (weeks.stream().anyMatch(cron -> cron.getWeek().getWeek().equals(day))) {
                        log.info("그 중 수행한 날인데 하지 않은 사람들 검거 완료");
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
            public void write(List<? extends Mission> items) throws Exception {
                log.info("어제 못한 미션 실패 횟수 카운트 1증가 완료");
                missionRepository.saveAll(items);
            }
        };
    }
}