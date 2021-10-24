package com.yapp.project.capture.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.CaptureContent;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.capture.CaptureTemplate;
import com.yapp.project.aux.test.mission.MissionTemplate;
import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.capture.domain.Capture;
import com.yapp.project.capture.domain.repository.CaptureImageRepository;
import com.yapp.project.capture.domain.repository.CaptureRepository;
import com.yapp.project.config.exception.capture.AlreadyExistsCaptureException;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yapp.project.aux.content.CaptureContent.CAPTURE_ALREADY_FINISH;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;
import static com.yapp.project.capture.domain.dto.CaptureDto.*;

@ExtendWith(MockitoExtension.class)
class CaptureServiceTest{
    CaptureServiceTest(){
        super();
    }
    @Mock
    private CaptureRepository captureRepository;

    @Mock
    private CaptureImageRepository captureImageRepository;

    @Mock
    private MissionRepository missionRepository;

    @InjectMocks
    private CaptureService captureService;

    @Test
    void test_오늘_사진_인증_성공_했을_때() {
        String imagePath = "home/image/capture";
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Mission mission = MissionTemplate.makeMission(account, organization);
        Long missionId = mission.getId();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        given(captureRepository.findByCreatedAtIsAfterAndMission_Id(todayMidnight, missionId)).willReturn(Optional.empty());
        given(missionRepository.findById(missionId)).willReturn(Optional.of(mission));
        CaptureResponseMessage message = captureService.captureTodayMission(imagePath, missionId);
        assertThat(message.getData().getResult()).isTrue();
    }

    @Test
    void test_오늘_사진_인증_성공_이미_했을_때() {
        String imagePath = "home/image/capture";
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Mission mission = MissionTemplate.makeMission(account, organization);
        Long missionId = mission.getId();
        Capture capture = CaptureTemplate.makeCapture(mission);
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        given(captureRepository.findByCreatedAtIsAfterAndMission_Id(todayMidnight, missionId)).willReturn(Optional.of(capture));
        assertThatThrownBy(() -> captureService.captureTodayMission(imagePath, missionId))
                .isInstanceOf(AlreadyExistsCaptureException.class).hasMessage(CAPTURE_ALREADY_FINISH);
    }


    @Test
    void test_미션_관련_내_사진_가져올_때() {
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Mission mission = MissionTemplate.makeMission(account, organization);
        int page = 0;
        int size = 3;
        Long missionId = mission.getId();
        List<Capture> captures = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            captures.add(CaptureTemplate.makeCapture(mission));
        }
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("createdAt").descending());
        given(captureRepository.findByMission_IdAndIsDeleteIsFalse(pageRequest, mission.getId())).willReturn(Optional.of(captures));
        CaptureListResponseMessage message = captureService.getMyMissionImages(missionId, page, size);
        assertThat(message.getMessage().getStatus()).isEqualTo(StatusEnum.CAPTURE_OK);
        assertThat(message.getMessage().getMsg()).isEqualTo(CaptureContent.CAPTURE_LIST_SUCCESS);
        int listSize = message.getData().getCaptures().size();
        assertThat(listSize).isEqualTo(3);
    }


    @Test
    void test_미션_관련_내_사진_가져오는데_아무것도_없을_때(){
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Mission mission = MissionTemplate.makeMission(account, organization);
        int page = 0;
        int size = 3;
        Long missionId = mission.getId();
        List<Capture> captures = Collections.emptyList();
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("createdAt").descending());
        given(captureRepository.findByMission_IdAndIsDeleteIsFalse(pageRequest, mission.getId())).willReturn(Optional.of(captures));
        CaptureListResponseMessage message = captureService.getMyMissionImages(missionId, page, size);
        assertThat(message.getMessage().getStatus()).isEqualTo(StatusEnum.CAPTURE_OK);
        assertThat(message.getMessage().getMsg()).isEqualTo(CaptureContent.CAPTURE_LIST_SUCCESS);
        int listSize = message.getData().getCaptures().size();
        assertThat(listSize).isZero();
    }


    @Test
    void test_과거_미션을_통해_했었던_이미지들을_지웠을_때(){
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Mission mission = MissionTemplate.makeMission(account, organization);
        List<Capture> captures = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            captures.add(CaptureTemplate.makeCapture(mission));
        }
        List<Long> captureIds = captures.stream().map(Capture::getId).collect(Collectors.toList());
        DeleteIdListRequest request = DeleteIdListRequest.builder()
                .captureIdLists(captureIds).build();
        CaptureResponseMessage message = captureService.deleteCaptureImages(request);
        assertThat(message.getMessage().getStatus()).isEqualTo(StatusEnum.CAPTURE_OK);
        assertThat(message.getMessage().getMsg()).isEqualTo(CaptureContent.CAPTURE_DELETE_SUCCESS);
        boolean result = message.getData().getResult();
        assertThat(result).isTrue();
    }


}