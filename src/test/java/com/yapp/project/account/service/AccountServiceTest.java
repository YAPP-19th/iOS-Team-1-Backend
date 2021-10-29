package com.yapp.project.account.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.dto.AccountDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.capture.domain.repository.CaptureImageRepository;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@WithMockUser(value=AccountTemplate.EMAIL, password = AccountTemplate.PASSWORD)
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    CaptureImageRepository captureImageRepository;


    @Test
    @Transactional
    void getUserInfo() {
        accountRepository.save(AccountTemplate.makeTestAccount());
        AccountDto.UserResponse accountUserResponseDto = accountService.getUserInfo();
        assertThat(accountUserResponseDto.getEmail()).isEqualTo(AccountTemplate.EMAIL);
    }

    @Test
    @Transactional
    void test_비밀번호_재설정(){
        Account account = accountRepository.save(AccountTemplate.makeTestAccount());
        String prevPassword = account.getPassword();
        AccountDto.ProfilePasswordRequest request = new AccountDto.ProfilePasswordRequest("resetTes23!");
        accountService.resetMyAccountPassword(request, account);
        Account dbAccount = accountRepository.findByEmail(account.getEmail()).orElse(null);
        assertThat(dbAccount).isNotNull();
        assertThat(dbAccount.getPassword()).isNotEqualTo(prevPassword);
    }

    @Test
    @Transactional
    void test_유저_삭제() throws NoSuchAlgorithmException {
        Account account = accountRepository.save(AccountTemplate.makeTestAccount());
        String email = account.getEmail();
        accountService.removeAccount(account);
        Account dbAccount = accountRepository.findByEmail(email).orElse(null);
        assertThat(dbAccount).isNull();
    }
}