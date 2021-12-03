package com.yapp.project.account.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.dto.AccountDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.capture.CaptureTemplate;
import com.yapp.project.aux.test.mission.MissionTemplate;
import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.capture.domain.Capture;
import com.yapp.project.capture.domain.repository.CaptureImageRepository;
import com.yapp.project.capture.domain.repository.CaptureRepository;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@WithMockUser(value=AccountTemplate.EMAIL, password = AccountTemplate.PASSWORD)
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private CaptureRepository captureRepository;

    @Autowired
    private CaptureImageRepository captureImageRepository;

    @Test
    @Transactional
    void test_알람토글_변경() {
        Account account = accountRepository.save(AccountTemplate.makeTestAccountForIntegration());
        assertThat(account.getIsAlarm()).isFalse();
        accountService.clickAlarmToggle(account);
        assertThat(account.getIsAlarm()).isTrue();
    }


    @Test
    @Transactional
    void getUserInfo() {
        accountRepository.save(AccountTemplate.makeTestAccountForIntegration());
        AccountDto.UserResponse accountUserResponseDto = accountService.getUserInfo();
        assertThat(accountUserResponseDto.getEmail()).isEqualTo(AccountTemplate.EMAIL);
    }

    @Test
    @Transactional
    void test_비밀번호_재설정(){
        Account account = accountRepository.save(AccountTemplate.makeTestAccountForIntegration());
        String prevPassword = account.getPassword();
        AccountDto.ProfilePasswordRequest request = new AccountDto.ProfilePasswordRequest("resetTes23!");
        accountService.resetMyAccountPassword(request, account);
        Account dbAccount = accountRepository.findByEmail(account.getEmail()).orElse(null);
        assertThat(dbAccount).isNotNull();
        assertThat(dbAccount.getPassword()).isNotEqualTo(prevPassword);
    }

    @Test
    @Transactional
    void test_유저_삭제() {
        Account account = accountRepository.save(AccountTemplate.makeTestAccountForIntegration());
        String email = account.getEmail();
        accountService.removeAccount(account);
        Account dbAccount = accountRepository.findByEmail(email).orElse(null);
        assertThat(dbAccount).isNull();
    }

    @Test
    @Transactional
    void test_유저_삭제_미션과_사진이_존재했을_때() {
        try(MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)) {
            dateUtil.when(DateUtil::KST_LOCAL_DATETIME_NOW).thenReturn(LocalDateTime.of(2021,11,9,6,30));
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.of(2021,11,9));
            dateUtil.when(DateUtil::MID_NIGHT).thenReturn(LocalDateTime.of(2021,11,9,0,0)); // 화요일
            Account account = accountRepository.save(AccountTemplate.makeTestAccountForIntegration());
            Organization organization = organizationRepository.save(OrganizationTemplate.makeTestOrganization());
            Mission mission = missionRepository.save(MissionTemplate.makeMission(account,organization));
            Capture capture =  captureRepository.save(CaptureTemplate.makeCapture(mission));
            String email = account.getEmail();
            accountService.removeAccount(account);
            Account dbAccount = accountRepository.findByEmail(email).orElse(null);
            assertThat(dbAccount).isNull();
            Mission afterMission = missionRepository.findAllByAccount(account).stream().findFirst().orElse(null);
            assertThat(afterMission).isNull();
            Capture afterCapture = captureRepository.findById(capture.getId()).orElse(null);
            assertThat(afterCapture).isNull();
            assertThat(captureImageRepository.findAll()).isEmpty();

        }

    }
}