package com.yapp.project.account.controller;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.dto.AccountRequestDto;
import com.yapp.project.account.domain.dto.AccountResponseDto;
import com.yapp.project.account.domain.dto.TokenDto;
import com.yapp.project.account.service.AuthService;
import com.yapp.project.aux.Message;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@WithMockUser(value = AccountTemplate.EMAIL, password = AccountTemplate.PASSWORD)
class AuthControllerTest {

    @Mock
    AuthService authService;


    @InjectMocks
    AuthController authController;

    @Test
    void signup() {
        Account account = AccountTemplate.makeTestAccount();
        AccountRequestDto accountRequestDto = AccountTemplate.makeAccountRequestDto();
        given(authService.signup(accountRequestDto)).willReturn(AccountResponseDto.of(account));
        ResponseEntity<Message> response = authController.signup(accountRequestDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getMsg()).isEqualTo("회원가입 축하드립니다.");
    }

    @Test
    void login() {
        AccountRequestDto accountRequestDto = AccountTemplate.makeAccountRequestDto();
        ResponseEntity<TokenDto> response = authController.login(accountRequestDto);
        assertThat(response).isNotNull();
    }

    @Test
    void logout() {
        ResponseEntity<Message> response = authController.logout();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

//    @Test
//    void reissue() {
//    }
}