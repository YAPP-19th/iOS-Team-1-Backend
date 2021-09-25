package com.yapp.project.account.service;
import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.Common;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@WithMockUser(value="springboot@example.com",password = "test1234",username = "스프링")
class PrincipalDetailServiceTest {

    @Autowired
    PrincipalDetailService principalDetailService;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void setUp(){
        if (accountRepository.findById(1L).isEmpty()){
            Account account = Common.makeTestAccount();
            accountRepository.save(account);
        }
    }

    @Test
    void loadUserByUserNameTest(){
        UserDetails userDetails = principalDetailService.loadUserByUsername("springboot@example.com");
        assertThat(userDetails.getUsername()).isEqualTo("1");
    }

    @Test
    void loadUserByUserNameFailureTest(){
        assertThatThrownBy(() -> principalDetailService.loadUserByUsername("fail@example.com")).isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("fail@example.com -> 데이터베이스에서 찾을 수 없습니다. ");
    }


}