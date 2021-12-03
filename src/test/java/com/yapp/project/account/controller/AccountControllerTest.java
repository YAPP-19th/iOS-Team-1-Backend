package com.yapp.project.account.controller;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.dto.AccountDto;
import com.yapp.project.account.service.AccountService;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.test.account.AccountTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

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
        Account account = AccountTemplate.makeTestAccountForIntegration();
        given(accountService.getUserInfo()).willReturn(AccountDto.UserResponse.of(account));
        AccountDto.UserResponseMessage response = accountController.getMyAccountInfo();
        assertThat(response).isNotNull();
        assertThat(response.getMessage().getStatus()).isEqualTo(StatusEnum.ACCOUNT_OK);
    }

}