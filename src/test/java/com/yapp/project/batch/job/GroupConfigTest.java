package com.yapp.project.batch.job;


import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.mission.MissionTemplate;
import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class GroupConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void test_그룹관련_배치_테스트() throws Exception {
        //given
        Account account = accountRepository.save(AccountTemplate.makeTestAccount2());
        Account account2 = accountRepository.save(AccountTemplate.makeTestAccount("user2", "hello@example.com"));
        Organization organization = organizationRepository.save(OrganizationTemplate.makeTestOrganization());
        LocalDate yesterday = DateUtil.KST_LOCAL_DATE_YESTERDAY();
        Mission mission = MissionTemplate.makeMission(account,organization, yesterday.minusDays(5), yesterday.plusDays(2));
        Mission mission2 = MissionTemplate.makeMission(account2,organization,yesterday.minusDays(14), yesterday);
        mission.setCountForTest();
        mission2.setCountForTest();
        missionRepository.save(mission);
        Mission finishMission = missionRepository.save(mission2);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("groupUpdateDate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        Organization dbOrganization = organizationRepository.findById(organization.getId()).orElse(null);
        assertThat(dbOrganization).isNotNull();
        assertThat(dbOrganization.getGroupSuccessCount()).isNotZero();
        assertThat(dbOrganization.getGroupFailCount()).isNotZero();
        assertThat(dbOrganization.getRate()).isNotZero();
        assertThat(dbOrganization.getUpdatedAt()).isEqualTo(DateUtil.KST_LOCAL_DATE_NOW());
        Mission dbFinishMission = missionRepository.findMissionByAccountAndId(account2,finishMission.getId()).orElse(null);
        assertThat(dbFinishMission).isNotNull();
        assertThat(dbFinishMission.getIsFinish()).isTrue();
    }
}