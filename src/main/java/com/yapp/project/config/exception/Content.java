package com.yapp.project.config.exception;

public class Content {
    private Content(){

    }
    public static final String NOT_FOUND_USER_INFORMATION = "유저 정보를 찾을 수 없습니다";
    public static final String LOGOUT_USER = "로그아웃된 사용자입니다.";
    public static final String EMAIL_DUPLICATE = "이미 가입되어 있는 유저입니다. ";
    public static final String NICKNAME_DUPLICATE = "이미 존재하는 닉네임 입니다. ";
    public static final String REFRESH_TOKEN_INVALID = "Refresh Token이 유효하지 않습니다.";
    public static final String TOKEN_NOT_EQUAL_USER_INFORMATION = "토큰의 유저 정보가 일치하지 않습니다. ";

}