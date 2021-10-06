package com.yapp.project.aux.note.account;

public class AccountNotes {
    private AccountNotes(){
    }
    public static final String SOCIAL_NOTES = "이미 존재하는 유저이면 data.token에 토큰 정보 전달" +
            "\n그렇지 않으면 유저정보 반환. 구분은 data.processes 로 진행(LOGIN, SIGNUP)" +
            "\nLOGIN은 TOKEN값을 응답, SIGNUP은 유저 이메일, 소셜타입 전달";
    public static final String SOCIAL_SIGNUP_NOTES = "소셜로그인 성공시 토큰 정보 전달(accessToken, refreshToken을 전달)";
}
