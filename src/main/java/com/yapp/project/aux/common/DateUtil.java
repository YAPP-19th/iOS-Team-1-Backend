package com.yapp.project.aux.common;

import java.time.*;
import java.time.format.DateTimeFormatter;

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

    public static LocalDate KST_LOCAL_DATE_YESTERDAY(){
        return KST_LOCAL_DATE_NOW().minus(Period.ofDays(1));
    }

    public static final int TEMP_NUMBER_SECONDS = 1000*60*3;

    public static LocalDate convertStr2LocalDate(String yyyymmdd){
        return LocalDate.parse(yyyymmdd, DateTimeFormatter.ISO_DATE);
    }
}
