package com.yapp.project.aux.test.account;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import com.yapp.project.account.domain.dto.AccountRequestDto;
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
    public static final String FIRST_USERNAME = "first";
    public static final String FIRST_EMAIL = "first@example.com";


    public static AccountRequestDto makeFirstUserRequestDto(){
        return new AccountRequestDto(FIRST_EMAIL,FIRST_USERNAME,PASSWORD);
    }

    public static Account makeTestAccount(){
        return Account.builder().id(id++).username(USERNAME).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email(EMAIL).createdAt(LocalDateTime.now()).lastLogin(LocalDateTime.now())
                .authority(Authority.ROLE_USER).build();
    }

    public static Account makeTestAccount(String username){
        return Account.builder().id(id++).username(username).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email(EMAIL).createdAt(LocalDateTime.now()).lastLogin(LocalDateTime.now())
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

    public static AccountRequestDto makeAccountRequestDto(){
        return new AccountRequestDto(EMAIL,USERNAME,PASSWORD);
    }

    public static AccountRequestDto makeAccountRequestDto(String email){
        return new AccountRequestDto(email,USERNAME,PASSWORD);
    }

    public static AccountRequestDto makeAccountRequestDto(String email,String username){
        return new AccountRequestDto(email,username,PASSWORD);
    }

}
