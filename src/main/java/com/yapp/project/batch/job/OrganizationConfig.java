package com.yapp.project.batch.job;

import com.yapp.project.mission.service.MissionService;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.service.GroupService;
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

import java.util.List;


@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class OrganizationConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MissionService missionService;
    private final GroupService groupService;

    @Bean(name="organizationJob")
    public Job participantsUpdateJob(){
        return jobBuilderFactory.get("participantsUpdate")
                .preventRestart()
                .start(participantsUpdateJobStep())
                .build();
    }

    @Bean
    public Step participantsUpdateJobStep(){
        return stepBuilderFactory.get("participantsUpdateJobStep")
                .<Organization, Organization> chunk(10)
                .reader(calculateOrganizationReader())
                .processor(updateOrganizationProcessor())
                .writer(organizationItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Organization> calculateOrganizationReader(){
        log.info("그룹 갯수를 세고 있습니다.");
        List<Organization> organizations = groupService.findAll();
        log.info("총 그룹 갯수는 "+organizations.size()+"개 입니다.");
        return new ListItemReader<>(organizations);
    }

    public ItemProcessor<Organization,Organization> updateOrganizationProcessor(){
        return item -> {
            Integer participants = missionService.getParticipantsFromOrganization(item);
            item.updateParticipants(participants);
            return item;
        };
    }

    public ItemWriter<Organization> organizationItemWriter(){
        return items -> groupService.saveAllOrganizations((List<Organization>) items);
    }


}
