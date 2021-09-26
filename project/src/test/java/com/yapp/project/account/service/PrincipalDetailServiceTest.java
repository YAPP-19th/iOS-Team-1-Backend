package com.yapp.project.account.service;
import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.test.account.AccountTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@WithMockUser(value="1",password = "test1234")
class PrincipalDetailServiceTest {

    @Autowired
    PrincipalDetailService principalDetailService;

    @Autowired
    AccountRepository accountRepository;


    @Test
    @Transactional
    void loadUserByUserNameTest(){
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);
        UserDetails userDetails = principalDetailService.loadUserByUsername(AccountTemplate.EMAIL);
        Account findAccount = accountRepository.findByEmail(AccountTemplate.EMAIL).orElse(null);
        assert findAccount != null;
        assertThat(Long.parseLong(userDetails.getUsername())).isEqualTo(findAccount.getId());
    }

    @Test
    void loadUserByUserNameFailureTest(){
        assertThatThrownBy(() -> principalDetailService.loadUserByUsername("fail@example.com")).isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("fail@example.com -> 데이터베이스에서 찾을 수 없습니다. ");
    }


}