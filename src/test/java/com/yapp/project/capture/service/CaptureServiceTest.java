package com.yapp.project.capture.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.content.CaptureContent;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.capture.CaptureTemplate;
import com.yapp.project.aux.test.mission.MissionTemplate;
import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.capture.domain.Capture;
import com.yapp.project.capture.domain.repository.CaptureImageRepository;
import com.yapp.project.capture.domain.repository.CaptureRepository;
import com.yapp.project.config.exception.capture.AlreadyExistsCaptureException;
import com.yapp.project.config.exception.capture.NotTodayCaptureException;
import com.yapp.project.mission.domain.Cron;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.routine.domain.Week;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;
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
        //given
        String imagePath = "home/image/capture";
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Mission mission = MissionTemplate.makeMission(account, organization);
        Long missionId = mission.getId();
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        String todayValue = today.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        List<Cron> weeks = new ArrayList<>();
        Cron cron = Cron.builder().week(Week.valueOf(todayValue)).mission(mission).build();
        weeks.add(cron);
        mission.setWeeksForTest(weeks);
        given(captureRepository.findByCreatedAtIsAfterAndMission_Id(todayMidnight, missionId)).willReturn(Optional.empty());
        given(missionRepository.findById(missionId)).willReturn(Optional.of(mission));
        //when
        CaptureResponseMessage message = captureService.captureTodayMission(imagePath, missionId);
        //then
        assertThat(message.getData().getResult()).isTrue();
    }

    @Test
    void test_오늘_사진_인증_성공_이미_했을_때() {
        //given
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
        //when -> then
        assertThatThrownBy(() -> captureService.captureTodayMission(imagePath, missionId))
                .isInstanceOf(AlreadyExistsCaptureException.class).hasMessage(CAPTURE_ALREADY_FINISH);
    }

    @Test
    void test_오늘_사진_인증_하는_날이_아닐_때(){
        try (MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)){
            //given
            dateUtil.when(DateUtil::KST_LOCAL_DATETIME_NOW).thenReturn(LocalDateTime.of(2021,11,9,6,30));
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.of(2021,11,9));
            dateUtil.when(DateUtil::MID_NIGHT).thenReturn(LocalDateTime.of(2021,11,9,0,0)); // 화요일
            String imagePath = "home/image/capture";
            Account account = AccountTemplate.makeTestAccount();
            Organization organization = OrganizationTemplate.makeTestOrganization();
            Mission mission = MissionTemplate.makeMission(account, organization);
            Long missionId = mission.getId();
            LocalDateTime today = DateUtil.MID_NIGHT();
            given(captureRepository.findByCreatedAtIsAfterAndMission_Id(today, missionId)).willReturn(Optional.empty());
            given(missionRepository.findById(missionId)).willReturn(Optional.of(mission));
            //when -> then
            assertThatThrownBy(() -> captureService.captureTodayMission(imagePath,missionId))
                    .isInstanceOf(NotTodayCaptureException.class)
                    .hasMessage(CaptureContent.CAPTURE_NOT_UPLOAD_DAY);
        }

    }


    @Test
    void test_미션_관련_내_사진_가져올_때() {
        //given
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
        given(captureRepository.findByMission_IdAndIsDeleteIsFalseOrderByCreatedAtDesc(pageRequest, mission.getId())).willReturn(Optional.of(captures));
        //when
        CaptureListResponseMessage message = captureService.getMyMissionImages(missionId, page, size,1);
        //then
        assertThat(message.getMessage().getStatus()).isEqualTo(StatusEnum.CAPTURE_OK);
        assertThat(message.getMessage().getMsg()).isEqualTo(CaptureContent.CAPTURE_LIST_SUCCESS);
        int listSize = message.getData().getCaptures().size();
        assertThat(listSize).isEqualTo(3);
    }


    @Test
    void test_미션_관련_내_사진_가져오는데_아무것도_없을_때(){
        //given
        Account account = AccountTemplate.makeTestAccount();
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Mission mission = MissionTemplate.makeMission(account, organization);
        int page = 0;
        int size = 3;
        Long missionId = mission.getId();
        List<Capture> captures = Collections.emptyList();
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("createdAt").descending());
        given(captureRepository.findByMission_IdAndIsDeleteIsFalseOrderByCreatedAtDesc(pageRequest, mission.getId())).willReturn(Optional.of(captures));
        //when
        CaptureListResponseMessage message = captureService.getMyMissionImages(missionId, page, size,1);
        //then
        assertThat(message.getMessage().getStatus()).isEqualTo(StatusEnum.CAPTURE_OK);
        assertThat(message.getMessage().getMsg()).isEqualTo(CaptureContent.CAPTURE_LIST_SUCCESS);
        int listSize = message.getData().getCaptures().size();
        assertThat(listSize).isZero();
    }


    @Test
    void test_과거_미션을_통해_했었던_이미지들을_지웠을_때(){
        //given
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
        //when
        CaptureResponseMessage message = captureService.deleteCaptureImages(request);
        //then
        assertThat(message.getMessage().getStatus()).isEqualTo(StatusEnum.CAPTURE_OK);
        assertThat(message.getMessage().getMsg()).isEqualTo(CaptureContent.CAPTURE_DELETE_SUCCESS);
        boolean result = message.getData().getResult();
        assertThat(result).isTrue();
    }

    @Test
    void test_그룹_관련_이미지_가져왔을_때(){
        //given
        Account account = AccountTemplate.makeTestAccount();
        Account account2 = AccountTemplate.makeTestAccount("second","secondary@example.com");
        Organization organization = OrganizationTemplate.makeTestOrganization();
        Mission mission = MissionTemplate.makeMission(account, organization);
        Mission mission2 = MissionTemplate.makeMission(account2, organization);
        int page = 0;
        int size = 6;
        Long organizationId = organization.getId();
        List<Capture> captures = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            captures.add(CaptureTemplate.makeCapture(mission));
            captures.add(CaptureTemplate.makeCapture(mission2));
        }
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("createdAt").descending());
        //when
        given(captureRepository.findByOrganization_IdAndIsDeleteIsFalseOrderByCreatedAtDesc(pageRequest, organization.getId())).willReturn(Optional.of(captures));
        //then
        CaptureListResponseMessage message = captureService.getOrganizationImages(organizationId,0,6,1);
        assertThat(message.getMessage().getStatus()).isEqualTo(StatusEnum.CAPTURE_OK);
        assertThat(message.getMessage().getMsg()).isEqualTo(CaptureContent.CAPTURE_LIST_SUCCESS);
        int listSize = message.getData().getCaptures().size();
        assertThat(listSize).isEqualTo(6);
    }

}