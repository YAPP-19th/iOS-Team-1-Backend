package com.yapp.project.mission.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private DateUtils(){
    }
    public static LocalDate convertStr2LocalDate(String yyyymmdd){
        return LocalDate.parse(yyyymmdd, DateTimeFormatter.ISO_DATE);
    }
}
