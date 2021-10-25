package com.yapp.project.account.domain;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATETIME_NOW;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class AccountTest {

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    Account account = Account.builder()
            .email("test@example.com")
            .password(bCryptPasswordEncoder.encode("test1234"))
            .lastLogin(KST_LOCAL_DATETIME_NOW())
            .createdAt(KST_LOCAL_DATETIME_NOW())
            .nickname("testMan").build();


    @Test
    void updateLastLoginAccount() {
        LocalDateTime dateTime = account.getLastLogin();
        account.updateLastLoginAccount();
        assertThat(dateTime).isNotEqualTo(account.getLastLogin());
    }

    @Test
    void getCreatedAt() {
        LocalDateTime date = KST_LOCAL_DATETIME_NOW();
        assertEquals(account.getLastLogin().getClass(),date.getClass());
    }
}