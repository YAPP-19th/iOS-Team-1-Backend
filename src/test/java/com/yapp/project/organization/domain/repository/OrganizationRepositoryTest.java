package com.yapp.project.organization.domain.repository;

import static com.yapp.project.aux.test.organization.OrganizationTemplate.*;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.capture.CaptureTemplate;
import com.yapp.project.aux.test.mission.MissionTemplate;
import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.capture.domain.Capture;
import com.yapp.project.capture.domain.repository.CaptureRepository;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private CaptureRepository captureRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void test_카테고리로_그룹_조회(){
        Organization organization = makeTestOrganizationForIntegration();
        Organization dbOrganization = organizationRepository.save(organization);
        List<Organization> organizations = organizationRepository.findByCategoryAndMore(CATEGORY);
        assertThat(organizations.get(0).getCategory()).isEqualTo(CATEGORY);
    }

    @Test
    void test_NOT_IN_쿼리확인() {
        Account account = AccountTemplate.makeTestAccountForIntegration();
        List<Organization> organizations = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            organizations.add(OrganizationTemplate.makeTestOrganizationForIntegration("테스트"+i));
        }
        Account newAccount = accountRepository.save(account);
        List<Organization> organizationList = organizationRepository.saveAll(organizations);
        List<Mission> missions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            missions.add(MissionTemplate.makeMissionForIntegration(newAccount,organizationList.get(i)));
        }
        List<Mission> missionList = missionRepository.saveAll(missions);
        List<Capture> captures = new ArrayList<>();
        for (Mission mission: missionList) {
            captures.add(CaptureTemplate.makeCapture(mission));
        }
        captureRepository.saveAll(captures);
        List<Long> excludeOrganizationIds = List.of(organizationList.get(0).getId());
        entityManager.clear();
        List<Organization> totalOrganizations = organizationRepository.findAll();
        List<Organization> orgs = organizationRepository.findOrganizationsNotIn(excludeOrganizationIds);
        assertThat(totalOrganizations.size() - orgs.size()).isEqualTo(1);
    }

    @Test
    void test_JPA_N_PLUS_1_문제를_해결하기_위한_테스트() {
        Account account = AccountTemplate.makeTestAccountForIntegration();
        List<Organization> organizations = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            organizations.add(OrganizationTemplate.makeTestOrganizationForIntegration("테스트"+i));
        }
        Account newAccount = accountRepository.save(account);
        List<Organization> organizationList = organizationRepository.saveAll(organizations);
        List<Mission> missions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            missions.add(MissionTemplate.makeMissionForIntegration(newAccount,organizationList.get(i)));
        }
        List<Mission> missionList = missionRepository.saveAll(missions);
        entityManager.clear();

        List<Mission> myMissions = missionRepository.findAllByAccount(newAccount);
        assertThat(myMissions.size()).isEqualTo(10);
    }
}