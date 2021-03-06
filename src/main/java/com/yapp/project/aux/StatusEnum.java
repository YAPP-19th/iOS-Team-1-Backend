package com.yapp.project.aux;

public enum StatusEnum {
    SOCIAL_OK(200, "SOCIAL_OK"),
    ACCOUNT_OK(200, "ACCOUNT_OK"),
    GROUP_OK(200, "GROUP_OK"),
    TOKEN_OK(200, "TOKEN_OK"),
    SAYING_OK(200, "SAYING_OK"),
    MISSION_OK(200, "MISSION_OK"),
    CAPTURE_OK(200, "CAPTURE_OK"),
    NOTIFICATION_OK(200, "NOTIFICATION_OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    ROUTINE_OK(200, "OK"),
    ROUTINE_BAD_REQUEST(400, "BAD_REQUEST"),
    ROUTINE_NOT_FOUND(404, "ROUTINE_NOT_FOUND"),
    REFRESH_TOKEN_NOT_IN_REDIS(404,"REFRESH_TOKEN_NOT_IN_REDIS"),
    EMAIL_BAD_REQUEST(400,"EMAIL_BAD_REQUEST"),
    NICKNAME_BAD_REQUEST(400,"NICKNAME_BAD_REQUEST"),
    USER_NOT_FOUND(404,"USER_NOT_FOUND"),
    PASSWORD_BAD_REQUEST(400,"PASSWORD_BAD_REQUEST"),
    REFRESH_BAD_REQUEST(400,"REFRESH_BAD_REQUEST"),
    AUTHENTICATION_NUMBER_BAD_REQUEST(400,"AUTHENTICATION_NUMBER_BAD_REQUEST"),
    TOKEN_BAD_REQUEST(400,"TOKEN_BAD_REQUEST"),
    SAYING_BAD_REQUEST(400,"SAYING_BAD_REQUEST"),
    SAYING_NOT_FOUND(404,"SAYING_NOT_FOUND"),
    MISSION_NOT_FOUND(404, "MISSION_NOT_FOUND"),
    MISSION_ALREADY_EXISTS(400, "MISSION_ALREADY_EXISTS"),
    RETROSPECT_OK(200, "RETROSPECT_OK"),
    RETROSPECT_BAD_REQUEST(400, "RETROSPECT_BAD_REQUEST"),
    RETROSPECT_NOT_FOUND(404, "RETROSPECT_NOT_FOUND"),
    CAPTURE_BAD_REQUEST(404, "CAPTURE_BAD_REQUEST"),
    WEEK_REPORT_BAD_REQUEST(400, "WEEK_REPORT_BAD_REQUEST"),
    WEEK_REPORT_NOT_FOUND(400, "WEEK_REPORT_BAD_REQUEST"),
    WEEK_REPORT_OK(200, "WEEK_REPORT_OK"),
    MONTH_REPORT_BAD_REQUEST(400, "MONTH_REPORT_BAD_REQUEST"),
    MONTH_REPORT_NOT_FOUND(404, "MONTH_REPORT_NOT_FOUND"),
    MONTH_REPORT_OK(200, "MONTH_REPORT_OK"),
    DAY_ROUTINE_RATE_OK(200, "DAY_ROUTINE_RATE_OK"),
    DAY_ROUTINE_RATE_BAD_REQUEST(400, "DAY_ROUTINE_RATE_BAD_REQUEST"),
    RECOMMENDED_ROUTINE_OK(200, "RECOMMENDED_ROUTINE_OK"),
    NOTICE_OK(200,"NOTICE_OK"),
    NOTICE_NOT_FOUND(200,"NOTICE_NOT_FOUND");

    final int statusCode;
    final String code;

    StatusEnum(int statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }
}
