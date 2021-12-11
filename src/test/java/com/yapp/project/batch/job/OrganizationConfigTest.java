package com.yapp.project.batch.job;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.mission.MissionTemplate;
import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrganizationConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtilsForParticipants;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Test
    void test_참여자_갯수_확인() {
        //given
        Account account = accountRepository.save(AccountTemplate.makeTestAccountForIntegration());
        Organization organization1 = organizationRepository.save(OrganizationTemplate.makeTestOrganizationForIntegration("1"));
        Organization organization2 = organizationRepository.save(OrganizationTemplate.makeTestOrganizationForIntegration("2"));
        missionRepository.save(MissionTemplate.makeMissionForIntegration(account, organization1));
        missionRepository.save(MissionTemplate.makeMissionForIntegration(account, organization2));
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("organizationUpdateRate", DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                .toJobParameters();

        //when
        JobExecution jobExecution;
        try {
            jobExecution = jobLauncherTestUtilsForParticipants.launchJob(jobParameters);
        } catch (Exception e) {
            jobExecution = null;
        }
        //then
        assertThat(jobExecution).isNotNull();
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        Organization afterOrg = organizationRepository.findById(organization1.getId()).orElse(null);
        assertThat(afterOrg).isNotNull();
        assertThat(afterOrg.getParticipants()).isOne();
        Organization afterOrg2 = organizationRepository.findById(organization2.getId()).orElse(null);
        assertThat(afterOrg2).isNotNull();
        assertThat(afterOrg2.getParticipants()).isOne();
        //after
        missionRepository.deleteAll();
        organizationRepository.deleteAll();
        accountRepository.deleteAll();
    }
}