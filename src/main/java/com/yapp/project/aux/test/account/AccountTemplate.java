package com.yapp.project.aux.test.account;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import com.yapp.project.account.domain.SocialType;
import com.yapp.project.account.domain.dto.AccountDto.*;
import com.yapp.project.account.domain.dto.SocialDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATETIME_NOW;

public class AccountTemplate {

    private AccountTemplate() {
        throw new IllegalArgumentException();
    }

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
  
    private static Long id = 100000L;
    public static final String PASSWORD = "Test1234!$";
    public static final String USERNAME = "스프링";
    public static final String ANOTHER_USERNAME = "장고";
    public static final String EMAIL = "springboot@example.com";
    public static final String ANOTHER_EMAIL = "django@example.com";
    public static final SocialType SOCIAL_TYPE = SocialType.NORMAL;
    public static final String FCM_TOKEN = "****====$$$$****";

    public static SocialDto.SocialSignUpRequest makeSocialSignUpRequest(){
        return SocialDto.SocialSignUpRequest.builder().email(EMAIL).nickname(USERNAME)
                .socialType(SocialType.KAKAO.toString()).build();
    }


    public static Account makeTestAccount(){
        return makeTestAccount(USERNAME,EMAIL,Authority.ROLE_USER, SOCIAL_TYPE);
    }

    public static Account makeTestAccountForIntegration(){
        return makeTestAccountForIntegration(USERNAME,EMAIL,Authority.ROLE_USER, SOCIAL_TYPE);
    }

    public static Account makeTestAccount2(){
        return makeTestAccount(ANOTHER_USERNAME,ANOTHER_EMAIL,Authority.ROLE_USER, SOCIAL_TYPE);
    }

    public static Account makeTestAccount(String username){
        return makeTestAccount(username,EMAIL,Authority.ROLE_USER, SOCIAL_TYPE);
    }

    public static Account makeTestAccount(SocialType socialType){
        return makeTestAccount(USERNAME,EMAIL,Authority.ROLE_USER, socialType);
    }

    public static Account makeTestAccount(String username, String email){
        return makeTestAccount(username,email,Authority.ROLE_USER, SOCIAL_TYPE);
    }

    public static Account makeTestAccountForIntegration(String username, String email){
        return makeTestAccountForIntegration(username,email,Authority.ROLE_USER, SOCIAL_TYPE);
    }

    public static Account makeTestAccount(String username, String email, Authority authority, SocialType socialType){
        Account account = Account.builder().id(id++).nickname(username).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email(email).createdAt(KST_LOCAL_DATETIME_NOW()).lastLogin(KST_LOCAL_DATETIME_NOW())
                .authority(authority).socialType(socialType).build();
        account.clickAlarmToggle();
        return account;
    }

    public static Account makeTestAccountForIntegration(String username, String email, Authority authority, SocialType socialType){
        Account account = Account.builder().nickname(username).password(bCryptPasswordEncoder.encode(PASSWORD))
                .email(email).createdAt(KST_LOCAL_DATETIME_NOW()).lastLogin(KST_LOCAL_DATETIME_NOW())
                .authority(authority).socialType(socialType).build();
        account.clickAlarmToggle();
        return account;
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
        return new UserRequest(email,username,password,socialType,FCM_TOKEN);
    }

}
