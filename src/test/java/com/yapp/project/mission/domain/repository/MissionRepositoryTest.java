package com.yapp.project.mission.domain.repository;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.mission.MissionTemplate;
import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.dao.MissionOrganization;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    @Transactional
    void test_유저로_끝나지_않고_삭제되지_않은_진행중인_그룹을_찾을_때(){
        //given
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Account dbAccount = accountRepository.save(account);
        Organization dbOrganization = organizationRepository.save(organization);
        Mission mission = MissionTemplate.makeMission(dbAccount, dbOrganization);
        missionRepository.save(mission);
        //when
        ArrayList<MissionOrganization> list = missionRepository.findMissionByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(dbAccount);
        //then
        Organization response = list.get(0).getOrganization();
        assertThat(organization.getId()).isEqualTo(response.getId());
        assertThat(organization.getCategory()).isEqualTo(response.getCategory());
        assertThat(organization.getRecommend()).isEqualTo(response.getRecommend());
    }

    @Test
    @Transactional
    void test_유저로_끝나지_않고_삭제되지_않은_미션을_찾을_때(){
        //given
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Account dbAccount = accountRepository.save(account);
        Organization dbOrganization = organizationRepository.save(organization);
        Mission mission = MissionTemplate.makeMission(dbAccount, dbOrganization);
        missionRepository.save(mission);
        //when
        List<Mission> list = missionRepository.findAllByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(dbAccount);
        Mission response = list.get(0);
        //then
        assertThat(response.getOrganization().getTitle()).isEqualTo(organization.getTitle());
        assertThat(response.getOrganization().getCategory()).isEqualTo(organization.getCategory());
        assertThat(response.getOrganization().getRecommend()).isEqualTo(organization.getRecommend());
        assertThat(response.getOrganization().getSummary()).isEqualTo(organization.getSummary());
        assertThat(response.getSuccessCount()).isZero();
        assertThat(response.getFailureCount()).isZero();
        assertThat(response.getIsDelete()).isFalse();
        assertThat(response.getIsFinish()).isFalse();
    }

    @Test
    void test_삭제되지_않고_끝나지_않은_그룹_아이디와_유저_아이디가_있는_것을_찾고자_했지만_이미_삭제가_되었을_때(){
        //given
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Account dbAccount = accountRepository.save(account);
        Organization dbOrganization = organizationRepository.save(organization);
        Mission mission = MissionTemplate.makeMission(dbAccount, dbOrganization);
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
