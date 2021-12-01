package com.yapp.project.report.template;

import com.yapp.project.account.domain.Account;
import com.yapp.project.routine.RoutineTemplate;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.Week;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeekReportTemplate {

    private WeekReportTemplate(){
    }
    private static Long id = 1L;

    public static List<Routine> makeWeek1Routine(Account account) {
        List<Routine> result = new ArrayList<>();
        List<Week> coffeeRoutineDays = new ArrayList<>();
        List<Week> readingRoutineDays = new ArrayList<>();
        coffeeRoutineDays.add(Week.MON); coffeeRoutineDays.add(Week.WED); coffeeRoutineDays.add(Week.FRI);
        readingRoutineDays.add(Week.TUE); readingRoutineDays.add(Week.WED); readingRoutineDays.add(Week.THU); readingRoutineDays.add(Week.SAT);
        Routine coffeeRoutine = RoutineTemplate.makeRoutine(account, "커피 내려 마시기", "커피 마시기", "생활", "2021-09-29 18:31:34", coffeeRoutineDays);
        Routine readingRoutine = RoutineTemplate.makeRoutine(account, "책 1챕터 읽기", "책 읽기", "생활", "2021-09-30 18:31:34", readingRoutineDays);
        result.add(coffeeRoutine); result.add(readingRoutine);
        return result;
    }

    public static List<Routine> makeWeek2Routine(Account account) {
        List<Routine> result = new ArrayList<>();
        result.addAll(makeWeek1Routine(account));
        List<Week> newRoutine = new ArrayList<>();
        newRoutine.add(Week.MON); newRoutine.add(Week.WED); newRoutine.add(Week.SAT); newRoutine.add(Week.SUN);
        Routine runningRoutine = RoutineTemplate.makeRoutine(account, "논스톱 러닝3Km", "달리기", "건강", "2021-10-09 18:31:34", newRoutine);
        result.add(runningRoutine);
        return result;
    }

    public static List<Routine> makeWeek3Routine(Account account) {
        List<Routine> result = new ArrayList<>();
        result.addAll(makeWeek2Routine(account));
        List<Week> newRoutine = new ArrayList<>();
        newRoutine.add(Week.WED); newRoutine.add(Week.SUN);
        Routine waterRoutine = RoutineTemplate.makeRoutine(account, "물 마시기", "마시기", "생활", "2021-10-13 18:31:34", newRoutine);
        result.add(waterRoutine);
        return result;
    }

    public static List<Routine> makeWeek4Routine(Account account) {
        List<Routine> result = new ArrayList<>();
        result.addAll(makeWeek3Routine(account));
        result.remove(2); // Delete Running Routine
        List<Week> newRoutine = new ArrayList<>();
        newRoutine.add(Week.WED); newRoutine.add(Week.FRI); newRoutine.add(Week.SUN);
        Routine meditationRoutine = RoutineTemplate.makeRoutine(account, "명상하기", "명상", "생활", "2021-10-22 18:31:34", newRoutine);
        result.add(meditationRoutine);
        return result;
    }

    public static List<Retrospect> makeWeek1Retrospect(List<Routine> routineList){
        List<Retrospect> result = new ArrayList<>();
        Retrospect coffeeRetrospect1 = makeRetrospect(routineList.get(0), Result.DONE, "2021-09-29");
        Retrospect coffeeRetrospect2 = makeRetrospect(routineList.get(0), Result.NOT, "2021-10-01");
        Retrospect readingRetrospect1 = makeRetrospect(routineList.get(1), Result.TRY, "2021-09-30");
        Retrospect readingRetrospect2 = makeRetrospect(routineList.get(1), Result.DONE, "2021-10-02");

        result.add(coffeeRetrospect1); result.add(coffeeRetrospect2); result.add(readingRetrospect1); result.add(readingRetrospect2);
        return result;
    }

    public static List<Retrospect> makeWeek2Retrospect(List<Routine> routineList){
        List<Retrospect> result = new ArrayList<>();
        Retrospect coffeeRetrospect1 = makeRetrospect(routineList.get(0), Result.DONE, "2021-10-04");
        Retrospect coffeeRetrospect2 = makeRetrospect(routineList.get(0), Result.NOT, "2021-10-06");
        Retrospect coffeeRetrospect3 = makeRetrospect(routineList.get(0), Result.TRY, "2021-10-08");
        Retrospect readingRetrospect1 = makeRetrospect(routineList.get(1), Result.NOT, "2021-10-05");
        Retrospect readingRetrospect2 = makeRetrospect(routineList.get(1), Result.DONE, "2021-10-06");
        Retrospect readingRetrospect3 = makeRetrospect(routineList.get(1), Result.TRY, "2021-10-07");
        Retrospect readingRetrospect4 = makeRetrospect(routineList.get(1), Result.DONE, "2021-10-09");
        Retrospect runningRetrospect1 = makeRetrospect(routineList.get(2), Result.DONE, "2021-10-09");
        Retrospect runningRetrospect2 = makeRetrospect(routineList.get(2), Result.DONE, "2021-10-10");

        result.add(coffeeRetrospect1); result.add(coffeeRetrospect2); result.add(coffeeRetrospect3);
        result.add(readingRetrospect1); result.add(readingRetrospect2); result.add(readingRetrospect3); result.add(readingRetrospect4);
        result.add(runningRetrospect1); result.add(runningRetrospect2);
        return result;
    }

    public static List<Retrospect> makeWeek3Retrospect(List<Routine> routineList){
        List<Retrospect> result = new ArrayList<>();
        Retrospect coffeeRetrospect1 = makeRetrospect(routineList.get(0), Result.NOT, "2021-10-11");
        Retrospect coffeeRetrospect2 = makeRetrospect(routineList.get(0), Result.NOT, "2021-10-13");
        Retrospect coffeeRetrospect3 = makeRetrospect(routineList.get(0), Result.DONE, "2021-10-15");
        Retrospect readingRetrospect1 = makeRetrospect(routineList.get(1), Result.TRY, "2021-10-12");
        Retrospect readingRetrospect2 = makeRetrospect(routineList.get(1), Result.NOT, "2021-10-13");
        Retrospect readingRetrospect3 = makeRetrospect(routineList.get(1), Result.TRY, "2021-10-14");
        Retrospect readingRetrospect4 = makeRetrospect(routineList.get(1), Result.DONE, "2021-10-16");
        Retrospect runningRetrospect1 = makeRetrospect(routineList.get(2), Result.DONE, "2021-10-11");
        Retrospect runningRetrospect2 = makeRetrospect(routineList.get(2), Result.DONE, "2021-10-13");
        Retrospect runningRetrospect3 = makeRetrospect(routineList.get(2), Result.DONE, "2021-10-16");
        Retrospect runningRetrospect4 = makeRetrospect(routineList.get(2), Result.DONE, "2021-10-17");
        Retrospect waterRetrospect1 = makeRetrospect(routineList.get(3), Result.DONE, "2021-10-13");
        Retrospect waterRetrospect2 = makeRetrospect(routineList.get(3), Result.DONE, "2021-10-17");

        result.add(coffeeRetrospect1); result.add(coffeeRetrospect2); result.add(coffeeRetrospect3);
        result.add(readingRetrospect1); result.add(readingRetrospect2); result.add(readingRetrospect3); result.add(readingRetrospect4);
        result.add(runningRetrospect1); result.add(runningRetrospect2); result.add(runningRetrospect3); result.add(runningRetrospect4);
        result.add(waterRetrospect1); result.add(waterRetrospect2);
        return result;
    }

    public static List<Retrospect> makeWeek4Retrospect(List<Routine> routineList){
        List<Retrospect> result = new ArrayList<>();
        Retrospect coffeeRetrospect1 = makeRetrospect(routineList.get(0), Result.NOT, "2021-10-18");
        Retrospect coffeeRetrospect2 = makeRetrospect(routineList.get(0), Result.NOT, "2021-10-20");
        Retrospect coffeeRetrospect3 = makeRetrospect(routineList.get(0), Result.DONE, "2021-10-22");
        Retrospect readingRetrospect1 = makeRetrospect(routineList.get(1), Result.TRY, "2021-10-19");
        Retrospect readingRetrospect2 = makeRetrospect(routineList.get(1), Result.NOT, "2021-10-20");
        Retrospect readingRetrospect3 = makeRetrospect(routineList.get(1), Result.TRY, "2021-10-21");
        Retrospect readingRetrospect4 = makeRetrospect(routineList.get(1), Result.DONE, "2021-10-23");
        Retrospect meditationRetrospect1 = makeRetrospect(routineList.get(2), Result.DONE, "2021-10-20");
        Retrospect meditationRetrospect2 = makeRetrospect(routineList.get(2), Result.DONE, "2021-10-24");
        Retrospect waterRetrospect1 = makeRetrospect(routineList.get(3), Result.DONE, "2021-10-22");
        Retrospect waterRetrospect2 = makeRetrospect(routineList.get(3), Result.DONE, "2021-10-24");

        result.add(coffeeRetrospect1); result.add(coffeeRetrospect2); result.add(coffeeRetrospect3);
        result.add(readingRetrospect1); result.add(readingRetrospect2); result.add(readingRetrospect3); result.add(readingRetrospect4);
        result.add(meditationRetrospect1); result.add(meditationRetrospect2);
        result.add(waterRetrospect1); result.add(waterRetrospect2);
        return result;
    }

    public static Retrospect makeRetrospect(Routine routine, Result result, String date) {
        Retrospect retrospect = Retrospect.builder().result(result).routine(routine).date(date).build();
        retrospect.updateTestData(id++, LocalDate.parse(date));
        return retrospect;
    }
}
