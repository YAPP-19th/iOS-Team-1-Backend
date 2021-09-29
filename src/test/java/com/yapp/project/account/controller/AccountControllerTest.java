package com.yapp.project.account.controller;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.dto.AccountResponseDto;
import com.yapp.project.account.service.AccountService;
import com.yapp.project.aux.test.account.AccountTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Objects;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@WithMockUser(value = AccountTemplate.EMAIL,password = AccountTemplate.PASSWORD)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void getMyAccountInfo() {
        Account account = AccountTemplate.makeTestAccount();
        given(accountService.getUserInfo()).willReturn(AccountResponseDto.of(account));
        ResponseEntity<AccountResponseDto> response = accountController.getMyAccountInfo();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAccountInfo() {
        Account account = AccountTemplate.makeTestAccount();
        given(accountService.getAccountInfo(AccountTemplate.EMAIL)).willReturn(AccountResponseDto.of(account));
        ResponseEntity<AccountResponseDto> response = accountController.getAccountInfo(AccountTemplate.EMAIL);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getEmail()).isEqualTo(AccountTemplate.EMAIL);
    }
}