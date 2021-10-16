package com.yapp.project.aux.test.account;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import com.yapp.project.account.domain.SocialType;
import com.yapp.project.account.domain.dto.AccountDto.*;
import com.yapp.project.account.domain.dto.SocialDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class AccountTemplate {

    private AccountTemplate() {
        throw new IllegalArgumentException();
    }

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private static Long id = 1L;
    public static final String PASSWORD = "Test1234!$";
    public static final String USERNAME = "스프링";
    public static final String EMAIL = "springboot@example.com";
    public static final SocialType SOCIAL_TYPE = SocialType.NORMAL;


    public static SocialDto.SocialSignUpRequest makeSocialSignUpRequest(){
        return SocialDto.SocialSignUpRequest.builder().email(EMAIL).nickname(USERNAME)
                .socialType(SocialType.KAKAO.toString()).build();
    }


    public static Account makeTestAccount(){
        return makeTestAccount(USERNAME,EMAIL,Authority.ROLE_USER);
    }

    public static Account makeTestAccount(String username){
        return makeTestAccount(username,EMAIL,Authority.ROLE_USER);
    }

    public static Account makeTestAccount(String username, String email){
        return makeTestAccount(username,email,Authority.ROLE_USER);
    }

    public static Account makeTestAccount(String username, String email, Authority authority){
        return Account.builder().id(id++).nickname(username).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email(email).createdAt(LocalDateTime.now()).lastLogin(LocalDateTime.now())
                .authority(authority).build();
    }

    public static UserRequest makeAccountRequestDto(){
        return makeAccountRequestDto(EMAIL,USERNAME,PASSWORD,SOCIAL_TYPE);
    }

    public static UserRequest makeAccountRequestDto(String email){
        return makeAccountRequestDto(email,USERNAME,PASSWORD,SOCIAL_TYPE);
    }

    public static UserRequest makeAccountRequestDto(String email, String username){
        return makeAccountRequestDto(email,username,PASSWORD,SOCIAL_TYPE);
    }

    public static UserRequest makeAccountRequestDto(String email, String username, String password){
        return makeAccountRequestDto(email,username,password,SOCIAL_TYPE);
    }

    public static UserRequest makeAccountRequestDto(String email, String username, String password, SocialType socialType){
        return new UserRequest(email,username,password,socialType);
    }

}
