package com.yapp.project.account.service;

import com.yapp.project.account.domain.dto.AccountDto;
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
    void getUserInfo() {
        accountRepository.save(AccountTemplate.makeTestAccount());
        AccountDto.UserResponse accountUserResponseDto = accountService.getUserInfo();
        assertThat(accountUserResponseDto.getEmail()).isEqualTo(AccountTemplate.EMAIL);
    }
}