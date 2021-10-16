package com.yapp.project.account.domain.dto;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.SocialType;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.*;

class AccountUserSocialRequestDtoTest {

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    AccountDto.UserRequest accountRequestDto = new AccountDto.UserRequest("test@example.com","test",
            "test1234", SocialType.NORMAL);

    @Test
    void toAccount() {
        Account account = accountRequestDto.toAccount(passwordEncoder);
        assertThat(account.getEmail()).isEqualTo(accountRequestDto.getEmail());
        assertThat(account.getNickname()).isEqualTo(accountRequestDto.getNickname());
        assertThat(account.getPassword()).isNotEqualTo(accountRequestDto.getPassword());
        assertThat(account.getLastLogin()).isNotNull();
        assertThat(account.getCreatedAt()).isNotNull();
    }

    @Test
    void toAuthentication() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = accountRequestDto.toAuthentication();
        assertThat(usernamePasswordAuthenticationToken.getName()).isEqualTo(accountRequestDto.getEmail());
        assertThat(usernamePasswordAuthenticationToken.getCredentials()).isEqualTo(accountRequestDto.getPassword());
    }
}