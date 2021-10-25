package com.yapp.project.aux.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class DateUtil {
    private DateUtil(){
    }
    private static final String ZONE = "Asia/Seoul";
    public static LocalDateTime MID_NIGHT(){
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of(ZONE));
        return LocalDateTime.of(today, midnight);
    }
    public static LocalDateTime KST_LOCAL_DATETIME_NOW(){
        return LocalDateTime.now(ZoneId.of(ZONE));
    }

    public static LocalDate KST_LOCAL_DATE_NOW(){
        return LocalDate.now(ZoneId.of(ZONE));
    }
}
