package com.yapp.project.mission.domain.repository;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.mission.MissionTemplate;
import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.dao.MissionOrganization;
import com.yapp.project.organization.domain.Clause;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MissionRepositoryTest {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void test_유저정보로_끝나지_않고_삭제되지_않은_미션_찾기() {
        try (MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)){
            //given
            dateUtil.when(DateUtil::KST_LOCAL_DATETIME_NOW).thenReturn(LocalDateTime.of(2021,12,4,18,10));
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.of(2021,12,4));
            dateUtil.when(DateUtil::MID_NIGHT).thenReturn(LocalDateTime.of(2021,12,4,0,0)); // 토요일
            Account account = AccountTemplate.makeTestAccountForIntegration();
            List<Organization> organizations = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                organizations.add(OrganizationTemplate.makeTestOrganizationForIntegration("테스트"+i));
            }
            Account dbAccount = accountRepository.save(account);
            List<Organization> dbOrganizations = organizationRepository.saveAll(organizations);
            List<Mission> missions = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                missions.add(MissionTemplate.makeMissionForIntegration(dbAccount, dbOrganizations.get(i)));
            }
            missions.add(MissionTemplate.makeMissionForIntegration(dbAccount,dbOrganizations.get(2),
                    DateUtil.KST_LOCAL_DATE_NOW().minus(Period.ofDays(14))
                    ,DateUtil.KST_LOCAL_DATE_NOW().minus(Period.ofDays(7))));
            List<Mission> dbMissions = missionRepository.saveAll(missions);
            Mission finishMission = dbMissions.get(2);
            finishMission.finishMission();
            missionRepository.saveAndFlush(finishMission);
            //saveAndFlush 한 이유는 이미 객체 안에 finishMission이라는 정보가 있기 때문에 트랜잭션 안에서 변화를 줘봤자
            // 객체가 그대로 있기 때문에 내가 진행하고자 하는 플로우를 보기 위해 flush를 했다.
            entityManager.clear();
            //when
            ArrayList<MissionOrganization> findIngMissions = missionRepository.findMissionByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(dbAccount);
            //then
            assertThat(findIngMissions.size()).isEqualTo(2);
        }
    }

    @Test
    @Transactional
    void test_날짜에_관한_쿼리_테스트() {
        try (MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)){
            //given
            dateUtil.when(DateUtil::KST_LOCAL_DATETIME_NOW).thenReturn(LocalDateTime.of(2021,12,4,5,0));
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.of(2021,12,4));
            dateUtil.when(DateUtil::MID_NIGHT).thenReturn(LocalDateTime.of(2021,12,4,0,0)); // 토요일
            Account account = AccountTemplate.makeTestAccountForIntegration();
            account.setIsAlarm(true);
            Clause clause = OrganizationTemplate.makeClauseForIntegration(LocalTime.of(5,0)
                    ,LocalTime.of(8,0));
            Organization organization = OrganizationTemplate
                    .makeTestOrganizationForIntegration("테스트",OrganizationTemplate.CATEGORY,clause);
            accountRepository.save(account);
            organizationRepository.save(organization);
            Mission mission = MissionTemplate.makeMissionForIntegration(account, organization);
            mission.clickAlarmToggle();
            mission.finishMission();
            mission.setStartTimeForTest(LocalTime.of(5,0));
            missionRepository.saveAndFlush(mission);
            entityManager.clear();
            //when
            List<Mission> dbMissions = missionRepository.findAllByIsDeleteIsFalseAndIsAlarmIsTrueAndStartTimeEquals(LocalTime.of(5,0));
            //then
            assertThat(dbMissions.size()).isEqualTo(1);
        }
    }

    @Test
    @Transactional
    void test_유저로_끝나지_않고_삭제되지_않은_진행중인_그룹을_찾을_때(){
        //given
        Account account = AccountTemplate.makeTestAccountForIntegration();
        Organization organization = OrganizationTemplate.makeTestOrganizationForIntegration();
        Account dbAccount = accountRepository.save(account);
        Organization dbOrganization = organizationRepository.save(organization);
        Mission mission = MissionTemplate.makeMissionForIntegration(dbAccount, dbOrganization);
        missionRepository.save(mission);
        //when
        ArrayList<MissionOrganization> list = missionRepository.findMissionByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(dbAccount);
        //then
        Organization response = list.get(0).getOrganization();
        assertThat(dbOrganization.getId()).isEqualTo(response.getId());
        assertThat(dbOrganization.getCategory()).isEqualTo(response.getCategory());
    }

    @Test
    @Transactional
    void test_유저로_끝나지_않고_삭제되지_않은_미션을_찾을_때(){
        //given
        Account account = AccountTemplate.makeTestAccountForIntegration();
        Organization organization = OrganizationTemplate.makeTestOrganizationForIntegration();
        Account dbAccount = accountRepository.save(account);
        Organization dbOrganization = organizationRepository.save(organization);
        Mission mission = MissionTemplate.makeMissionForIntegration(dbAccount, dbOrganization);
        missionRepository.save(mission);
        //when
        List<Mission> list = missionRepository.findAllByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(dbAccount);
        Mission response = list.get(0);
        //then
        assertThat(response.getOrganization().getTitle()).isEqualTo(dbOrganization.getTitle());
        assertThat(response.getOrganization().getCategory()).isEqualTo(dbOrganization.getCategory());
        assertThat(response.getSuccessCount()).isZero();
        assertThat(response.getFailureCount()).isZero();
        assertThat(response.getIsDelete()).isFalse();
        assertThat(response.getIsFinish()).isFalse();
    }

    @Test
    @Transactional
    void test_삭제되지_않고_끝나지_않은_그룹_아이디와_유저_아이디가_있는_것을_찾고자_했지만_이미_삭제가_되었을_때(){
        //given
        Account account = AccountTemplate.makeTestAccountForIntegration();
        Organization organization = OrganizationTemplate.makeTestOrganizationForIntegration();
        Account dbAccount = accountRepository.save(account);
        Organization dbOrganization = organizationRepository.save(organization);
        Mission mission = MissionTemplate.makeMissionForIntegration(dbAccount, dbOrganization);
        mission.remove();
        missionRepository.save(mission);
        //when
        Mission response = missionRepository
                .findMissionByAccountAndOrganization_IdAndIsFinishIsFalseAndIsDeleteIsFalse(dbAccount, dbOrganization.getId())
                .orElse(null);
        //then
        assertThat(response).isNull();
    }


}
