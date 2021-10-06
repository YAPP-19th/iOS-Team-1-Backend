package com.yapp.project.aux.test.account;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import com.yapp.project.account.domain.dto.AccountDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class AccountTemplate {

    private AccountTemplate() {
        throw new IllegalArgumentException();
    }

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private static Long id = 1L;
    public static final String PASSWORD = "test1234";
    public static final String USERNAME = "스프링";
    public static final String EMAIL = "springboot@example.com";


    public static Account makeTestAccount(){
        return Account.builder().id(id++).nickname(USERNAME).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email(EMAIL).createdAt(LocalDateTime.now()).lastLogin(LocalDateTime.now())
                .authority(Authority.ROLE_USER).build();
    }

    public static Account makeTestAccount(String username){
        return Account.builder().id(id++).nickname(username).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email(EMAIL).createdAt(LocalDateTime.now()).lastLogin(LocalDateTime.now())
                .authority(Authority.ROLE_USER).build();
    }

    public static Account makeTestAccount(String username, String email){
        return Account.builder().id(id++).nickname(username).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email(email).createdAt(LocalDateTime.now()).lastLogin(LocalDateTime.now())
                .authority(Authority.ROLE_USER).build();
    }

    public static Account makeTestAccount(String username, String email, Authority authority){
        return Account.builder().id(id++).nickname(username).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email(email).createdAt(LocalDateTime.now()).lastLogin(LocalDateTime.now())
                .authority(authority).build();
    }

    public static AccountDto.Request makeAccountRequestDto(){
        return new AccountDto.Request(EMAIL,USERNAME,PASSWORD);
    }

    public static AccountDto.Request makeAccountRequestDto(String email){
        return new AccountDto.Request(email,USERNAME,PASSWORD);
    }

    public static AccountDto.Request makeAccountRequestDto(String email,String username){
        return new AccountDto.Request(email,username,PASSWORD);
    }

}
