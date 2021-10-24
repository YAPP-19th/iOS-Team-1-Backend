package com.yapp.project.aux.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class DateUtil {
    private DateUtil(){
    }
    public static LocalDateTime MID_NIGHT(){
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        return LocalDateTime.of(today, midnight);
    }
}
