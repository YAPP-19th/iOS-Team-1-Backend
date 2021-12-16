package com.yapp.project.routine.domain;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public enum Week {
    MON("MON", 0),
    TUE("TUE", 1),
    WED("WED", 2),
    THU("THU", 3),
    FRI("FRI", 4),
    SAT("SAT", 5),
    SUN("SUN", 6);

    private final String week;
    private final int index;

    Week(String week, int index) {
        this.week = week;
        this.index = index;
    }

    public String getWeek() {
        return week;
    }

    public int getIndex() {
        return index;
    }

    @NotNull
    public static Week getWeek(String date) {
        String strDay = LocalDate.parse(date).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
        return Week.valueOf(strDay);
    }

}
