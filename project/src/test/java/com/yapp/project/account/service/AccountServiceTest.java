package com.yapp.project.account.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.dto.AccountResponseDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.Common;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@WithMockUser(value="1",password = "test1234")
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    private final String email = "meaning@example.com";

    private Account account;

    @BeforeEach
    void setup(){
        if(accountRepository.findById(1L).isEmpty()){
            String username = "미닝";
            account = Common.makeTestAccount(username, email);
            accountRepository.save(account);
        }
    }


    @Test
    void getAccountInfo() {
        AccountResponseDto accountResponseDto = accountService.getAccountInfo(email);
        assertThat(accountResponseDto.getEmail()).isEqualTo(account.getEmail());
    }

    @Test
    void getAccountInfoFail(){
        assertThatThrownBy(() -> accountService.getAccountInfo("fail@example.com"))
                .isInstanceOf(RuntimeException.class).hasMessage("유저 정보가 없습니다.");
    }

    @Test
    void getUserInfo() {
        AccountResponseDto accountResponseDto = accountService.getUserInfo();
        assertThat(accountResponseDto.getEmail()).isEqualTo(account.getEmail());
    }
}