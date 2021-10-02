package com.yapp.project.account.domain;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class AccountTest {

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    Account account = Account.builder()
            .email("test@example.com")
            .password(bCryptPasswordEncoder.encode("test1234"))
            .lastLogin(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .nickname("testMan").build();


    @Test
    void updateLastLoginAccount() {
        LocalDateTime dateTime = account.getLastLogin();
        account.updateLastLoginAccount();
        assertThat(dateTime).isNotEqualTo(account.getLastLogin());
    }

    @Test
    void getCreatedAt() {
        LocalDateTime date = LocalDateTime.now();
        assertEquals(account.getLastLogin().getClass(),date.getClass());
    }
}