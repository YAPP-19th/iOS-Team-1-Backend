package com.yapp.project.batch.job;

import com.yapp.project.aux.alert.AlertService;
import com.yapp.project.aux.alert.SlackChannel;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.mission.service.MissionService;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import com.yapp.project.organization.service.GroupService;
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
import java.util.List;


@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class GroupConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AlertService alertService;
    private final MissionService missionService;
    private final GroupService groupService;

    @Bean(name="groupJob")
    public Job achievementRateJob(){
        return jobBuilderFactory.get("achievementRate")
                .preventRestart()
                .start(achievementRateJobStep())
                .build();
    }

    @Bean
    public Step achievementRateJobStep(){
        return stepBuilderFactory.get("achievementRateJobStep")
                .<Mission, Organization> chunk(10)
                .reader(calculateCountReader())
                .processor(updateOrganizationProcessor())
                .writer(organizationItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Mission> calculateCountReader(){
        log.info("미션 성공/실패 횟수를 읽고 있습니다.");
        List<Mission> missions = missionService.getMissionIsDeleteIsFalse();
        log.info("총 미션 갯수는 "+missions.size()+"개 입니다.");
        alertService.slackSendMessage(SlackChannel.BATCH,"총 미션 갯수는 "+missions.size()+"개 입니다.");
        return new ListItemReader<>(missions);
    }

    public ItemProcessor<Mission,Organization> updateOrganizationProcessor(){
        return new ItemProcessor<Mission, Organization>() {
            @Override
            public Organization process(@NotNull Mission item) throws Exception {
                if (item.getFinishDate().isEqual(DateUtil.KST_LOCAL_DATE_YESTERDAY())){
                    log.info("오늘 기준으로 어제가 마지막인 미션은 종료설정 하기");
                    missionService.setFinishIfYesterdayIsLastDay(item);
                }
                Organization organization = item.getOrganization();
                if (organization.getUpdatedAt().equals(DateUtil.KST_LOCAL_DATE_YESTERDAY())){
                    log.info("처음에 업데이트하고자 했을 때 날짜와 갯수 초기화");
                    groupService.updateOrganizationCountAndUpdateAt(organization);
                }
                log.info("그룹 성공/실패 갯수 갱신");
                groupService.addMissionRateOnGroup(organization, item);
                return organization;
            }
        };
    }

    public ItemWriter<Organization> organizationItemWriter(){
        return new ItemWriter<Organization>() {
            @Override
            public void write(@NotNull List<? extends Organization> items) throws Exception {
                for (Organization organization : items){
                    log.info("갯수를 통해 달성률 계산");
                    organization.updateRate();
                }
                groupService.saveAllOrganizations((List<Organization>) items);
            }
        };
    }


}
