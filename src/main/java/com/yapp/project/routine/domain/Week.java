package com.yapp.project.routine.domain;

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

    public static Week getIndexOfValue(int index){
        switch (index){
            case 1:
                return Week.TUE;
            case 2:
                return Week.WED;
            case 3:
                return Week.THU;
            case 4:
                return Week.FRI;
            case 5:
                return Week.SAT;
            case 6:
                return Week.SUN;
            default:
                return Week.MON;
        }
    }
}
