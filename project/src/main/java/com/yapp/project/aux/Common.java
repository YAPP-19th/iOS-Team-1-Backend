package com.yapp.project.aux;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class Common {

    private Common() {
        throw new IllegalArgumentException();
    }

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private static Long id = 1L;
    private static final String PASSWORD = "test1234";

    public static Account makeTestAccount(){
        return Account.builder().id(id++).username("스프링").password(bCryptPasswordEncoder.encode(PASSWORD))
                .email("springboot@example.com").createdAt(LocalDateTime.now()).lastLogin(LocalDateTime.now())
                .authority(Authority.ROLE_USER).build();
    }

    public static Account makeTestAccount(String username){
        return Account.builder().id(id++).username(username).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email("springboot@example.com").createdAt(LocalDateTime.now()).lastLogin(LocalDateTime.now())
                .authority(Authority.ROLE_USER).build();
    }

    public static Account makeTestAccount(String username, String email){
        return Account.builder().id(id++).username(username).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email(email).createdAt(LocalDateTime.now()).lastLogin(LocalDateTime.now())
                .authority(Authority.ROLE_USER).build();
    }

    public static Account makeTestAccount(String username, String email, Authority authority){
        return Account.builder().id(id++).username(username).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email(email).createdAt(LocalDateTime.now()).lastLogin(LocalDateTime.now())
                .authority(authority).build();
    }

}
