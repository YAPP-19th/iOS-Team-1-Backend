package com.yapp.project.batch.job;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.mission.MissionTemplate;
import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.mission.domain.Cron;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import com.yapp.project.routine.domain.Week;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MissionConfigTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtilsForMission;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void test_미션_실패_여부_테스트(){
        try (MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)){
            //given
            dateUtil.when(DateUtil::KST_LOCAL_DATETIME_NOW).thenReturn(LocalDateTime.of(2021,11,10,12,1));
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.of(2021,11,10));
            dateUtil.when(DateUtil::MID_NIGHT).thenReturn(LocalDateTime.of(2021,11,10,0,0)); // 수요일
            Organization organization = organizationRepository.save(OrganizationTemplate.makeTestOrganization());
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("missionUpdateDate",DateUtil.KST_LOCAL_DATETIME_NOW().toString())
                    .toJobParameters();
            Mission notFailureMission = getNotFailureMission(organization);
            Mission failureMission = getFailureMission(organization);
            //when
            JobExecution jobExecution = jobLauncherTestUtilsForMission.launchJob(jobParameters);
            //then
            assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
            Mission findFailureMission = missionRepository.findById(failureMission.getId()).orElse(null);
            Mission findNoFailureMission = missionRepository.findById(notFailureMission.getId()).orElse(null);
            assertThat(findFailureMission).isNotNull();
            assertThat(findNoFailureMission).isNotNull();
            assertThat(findFailureMission.getFailureCount()).isGreaterThan(failureMission.getFailureCount());
            assertThat(findFailureMission.getFailureCount()).isEqualTo(failureMission.getFailureCount());
            //delete
            missionRepository.deleteAll();
            organizationRepository.delete(organization);
            accountRepository.deleteAll();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

    private Mission getNotFailureMission(Organization organization){
        Account account = accountRepository.save(AccountTemplate.makeTestAccount2());
        LocalDate yesterday = DateUtil.KST_LOCAL_DATE_YESTERDAY();
        Mission mission = MissionTemplate.makeMission(account,organization, yesterday.minusDays(5), yesterday.plusDays(2));
        List<Cron> list = new ArrayList<>();
        Cron cron1 = Cron.builder().mission(mission).week(Week.THU).build();
        Cron cron2 = Cron.builder().mission(mission).week(Week.WED).build();
        list.add(cron1);
        list.add(cron2);
        mission.setWeeksForTest(list);
        mission.setCountForTest();
        return missionRepository.save(mission);
    }

    private Mission getFailureMission(Organization organization){
        Account account = accountRepository.save(AccountTemplate.makeTestAccount("test", "test@minning.com"));
        LocalDate yesterday = DateUtil.KST_LOCAL_DATE_YESTERDAY();
        Mission mission = MissionTemplate.makeMission(account,organization,yesterday.minusDays(9), yesterday.plusDays(5));
        List<Cron> list2 = new ArrayList<>();
        Cron cron = Cron.builder().mission(mission).week(Week.TUE).build();
        Cron cron2 = Cron.builder().mission(mission).week(Week.MON).build();
        list2.add(cron);
        list2.add(cron2);
        mission.setWeeksForTest(list2);
        mission.setCountForTest();
        return missionRepository.save(mission);
    }
}
