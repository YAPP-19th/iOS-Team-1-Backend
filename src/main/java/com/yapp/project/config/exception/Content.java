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
    public static final String NOT_VAILDATION_PASSWORD = "특수문자,숫자,영 소문자 대문자 최소 1개의 문자를 넣어 최소8개의 패스워드를 작성해주세요.";

    // Saying Error Message
    public static final String OVER_SIZE_ID_NUMBER = "속담 갯수를 초과했습니다.";
    public static final String ALREADY_FOUND_SAYING_RECORD = "이미 생성된  유저-명언 데이터 입니다.";

    // Routine Error Message
    public static final String BAD_REQUEST_CREATE_ROUTINE_DATA = "요청 데이터를 확인하세요.";
    public static final String BAD_REQUEST_GET_ROUTINE_ID = "루틴 ID를 확인하세요.";
    public static final String NOT_FOUND_ROUTINE = "루틴이 존재하지 않습니다.";

}