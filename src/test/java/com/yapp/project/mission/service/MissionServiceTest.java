package com.yapp.project.mission.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.mission.MissionTemplate;
import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.config.exception.mission.AlreadyMissionExistException;
import com.yapp.project.aux.content.MissionContent;
import com.yapp.project.config.exception.mission.MissionNotFoundException;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.dto.MissionDto;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MissionServiceTest {
    @Mock
    private MissionRepository missionRepository;


    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private MissionService missionService;


    @Test
    void test_미션_생성() {
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        MissionDto.MissionRequest request = MissionTemplate.makeMissionRequest();
        given(organizationRepository.getById(request.getId())).willReturn(organization);
        Message message = missionService.createMission(request,account);
        assertThat(message.getStatus()).isEqualTo(StatusEnum.MISSION_OK);
    }

    @Test
    void test_이미_유저_그룹_관계가_있을_때(){
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        MissionDto.MissionRequest request = MissionTemplate.makeMissionRequest();

        Mission mission = request.toMission(account,organization);
        given(missionRepository.findMissionByAccountAndOrganization_IdAndIsFinishIsFalse(account, request.getId()))
                .willReturn(Optional.of(mission));

        assertThatThrownBy(() -> missionService.createMission(request, account)).isInstanceOf(AlreadyMissionExistException.class)
                .hasMessage(MissionContent.MISSION_BAD_REQUEST);
    }

    @Test
    void test_현재_진행_중인_미션이_있을_때(){
        Account account = AccountTemplate.makeTestAccount();

        List<Organization> organizations = new ArrayList<>();
        organizations.add(OrganizationTemplate.makeTestOrganization("확언하기","미라클모닝"));
        organizations.add(OrganizationTemplate.makeTestOrganization("시각화","미라클모닝"));
        organizations.add(OrganizationTemplate.makeTestOrganization("감사한 일 쓰기","미라클모닝"));

        List<Mission> missions = new ArrayList<>();
        missions.add(MissionTemplate.makeMissionRequest().toMission(account,organizations.get(0)));
        missions.add(MissionTemplate.makeMissionRequest().toMission(account,organizations.get(1)));
        missions.add(MissionTemplate.makeMissionRequest().toMission(account,organizations.get(2)));

        given(missionRepository.findAllByAccountAndIsFinishIsFalse(account)).willReturn(missions);

        MissionDto.MissionResponseMessage message = missionService.findAllIsDoing(account);

        assertThat(message.getMessage().getStatus()).isEqualTo(StatusEnum.MISSION_OK);
        assertThat(message.getData().get(0).getTitle()).isEqualTo("확언하기");
        assertThat(message.getData().get(1).getTitle()).isEqualTo("시각화");
        assertThat(message.getData().get(2).getTitle()).isEqualTo("감사한 일 쓰기");
    }

    @Test
    void test_미션_디테일_페이지_내용이_있을_때(){
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Mission mission = MissionTemplate.makeMissionRequest().toMission(account,organization);
        given(missionRepository.findMissionByAccountAndId(account,1L))
                .willReturn(Optional.of(mission));
        MissionDto.MissionDetailResponseMessage responseMessage = missionService.findDetailMyMission(account,1L);
        assertThat(responseMessage.getMessage().getStatus()).isEqualTo(StatusEnum.MISSION_OK);
        assertThat(responseMessage.getData().getPeriod()).isEqualTo(7);
    }

    @Test
    void test_미션_디테일_페이지_내용이_없을_때(){
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        given(missionRepository.findMissionByAccountAndId(account,1L)).willReturn(Optional.empty());
        assertThatThrownBy(() ->missionService.findDetailMyMission(account,1L)).isInstanceOf(MissionNotFoundException.class)
                .hasMessage(MissionContent.MISSION_NOT_FOUND);
    }

}