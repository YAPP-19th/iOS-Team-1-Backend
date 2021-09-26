package com.yapp.project.account.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.dto.AccountResponseDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.test.account.AccountTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@WithMockUser(value=AccountTemplate.EMAIL, password = AccountTemplate.PASSWORD)
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    @Transactional
    void getAccountInfo() {
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);
        AccountResponseDto accountResponseDto = accountService.getAccountInfo(AccountTemplate.EMAIL);
        assertThat(accountResponseDto.getEmail()).isEqualTo(account.getEmail());
    }

    @Test
    void getAccountInfoFail(){
        assertThatThrownBy(() -> accountService.getAccountInfo("fail@example.com"))
                .isInstanceOf(RuntimeException.class).hasMessage("유저 정보가 없습니다.");
    }

    @Test
    @Transactional
    void getUserInfo() {
        accountRepository.save(AccountTemplate.makeTestAccount());
        AccountResponseDto accountResponseDto = accountService.getUserInfo();
        assertThat(accountResponseDto.getEmail()).isEqualTo(AccountTemplate.EMAIL);
    }
}