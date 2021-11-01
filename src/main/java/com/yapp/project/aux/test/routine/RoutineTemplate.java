package com.yapp.project.aux.test.routine;

import com.yapp.project.account.domain.Account;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.domain.RoutineDay;
import com.yapp.project.routine.domain.Week;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoutineTemplate {
    private RoutineTemplate(){
    }
    private static Long routineId = 1L;
    private static Long retrospectId = 1L;

    public static Routine makeCoffeeRoutine(Account account) {
        List<Week> coffeeDays = new ArrayList<>();
        coffeeDays.add(Week.WED); coffeeDays.add(Week.SUN);
        RoutineDTO.RequestRoutineDto newRoutine1 = new RoutineDTO.RequestRoutineDto("커피", "커피 내려 마시", coffeeDays, "07:35", "생활");
        Routine coffeeRoutine = Routine.builder().newRoutine(newRoutine1).id(routineId++).account(account).build();
        List<RoutineDay> routineDays = coffeeDays.stream().map(day -> RoutineDay.builder().day(day).routine(coffeeRoutine).build()).collect(Collectors.toList());
        coffeeRoutine.addDays(routineDays);
        coffeeRoutine.updateCreateAt(LocalDateTime.parse("2021-10-17 18:31:34",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return coffeeRoutine;
    }

    public static List<Retrospect> makeCoffeeRetrospectList(Routine coffeeRoutine) {
        List<Retrospect> retrospectList = new ArrayList<>();
        Retrospect sunDay = Retrospect.builder().content("").routine(coffeeRoutine).result(Result.DONE).build();
        sunDay.updateTestData(retrospectId++, LocalDate.parse("2021-10-24"));
        retrospectList.add(sunDay);
        return retrospectList;
    }

    public static Routine makeReadingRoutine(Account account) {
        List<Week> readingDays = new ArrayList<>();
        readingDays.add(Week.THU); readingDays.add(Week.FRI);
        RoutineDTO.RequestRoutineDto newRoutine1 = new RoutineDTO.RequestRoutineDto("책읽기", "1챕터씩 읽기", readingDays, "07:35", "생활");
        Routine readingRoutine = Routine.builder().newRoutine(newRoutine1).id(routineId++).account(account).build();
        List<RoutineDay> routineDays = readingDays.stream().map(day -> RoutineDay.builder().day(day).routine(readingRoutine).build()).collect(Collectors.toList());
        readingRoutine.addDays(routineDays);
        readingRoutine.updateCreateAt(LocalDateTime.parse("2021-10-22 18:31:34",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return readingRoutine;
    }

    public static List<Retrospect> makeReadingRetrospectList(Routine readingRoutine) {
        List<Retrospect> retrospectList = new ArrayList<>();
        Retrospect friDay = Retrospect.builder().content("").routine(readingRoutine).result(Result.DONE).build();
        friDay.updateTestData(retrospectId++, LocalDate.parse("2021-10-22"));
        retrospectList.add(friDay);
        return retrospectList;
    }

    public static Routine makeRunningRoutine(Account account) {
        List<Week> runningDays = new ArrayList<>();
        runningDays.add(Week.TUE); runningDays.add(Week.WED); runningDays.add(Week.THU); runningDays.add(Week.FRI);
        RoutineDTO.RequestRoutineDto newRoutine1 = new RoutineDTO.RequestRoutineDto("달리기", "논스톱 달리기", runningDays, "07:35", "건강");
        Routine runningRoutine = Routine.builder().newRoutine(newRoutine1).id(routineId++).account(account).build();
        List<RoutineDay> routineDays = runningDays.stream().map(day -> RoutineDay.builder().day(day).routine(runningRoutine).build()).collect(Collectors.toList());
        runningRoutine.addDays(routineDays);
        runningRoutine.updateCreateAt(LocalDateTime.parse("2021-10-19 18:31:34",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return runningRoutine;
    }

    public static List<Retrospect> makeRunningRetrospectList(Routine runningRoutine) {
        List<Retrospect> retrospectList = new ArrayList<>();
        Retrospect wedDay = Retrospect.builder().content("").routine(runningRoutine).result(Result.DONE).build();
        wedDay.updateTestData(retrospectId++, LocalDate.parse("2021-10-20"));
        Retrospect friDay = Retrospect.builder().content("").routine(runningRoutine).result(Result.TRY).build();
        friDay.updateTestData(retrospectId++, LocalDate.parse("2021-10-21"));
        retrospectList.add(wedDay); retrospectList.add(friDay);
        return retrospectList;
    }

    public static Routine makeWaterRoutine(Account account) {
        List<Week> waterDays = new ArrayList<>();
        waterDays.add(Week.MON); waterDays.add(Week.TUE); waterDays.add(Week.WED); waterDays.add(Week.THU);
        waterDays.add(Week.FRI); waterDays.add(Week.SAT); waterDays.add(Week.SUN);
        RoutineDTO.RequestRoutineDto newRoutine1 = new RoutineDTO.RequestRoutineDto("물마시기", "물 꼭 마시기", waterDays, "07:35", "건강");
        Routine waterRoutine = Routine.builder().newRoutine(newRoutine1).id(routineId++).account(account).build();
        List<RoutineDay> routineDays = waterDays.stream().map(day -> RoutineDay.builder().day(day).routine(waterRoutine).build()).collect(Collectors.toList());
        waterRoutine.addDays(routineDays);
        waterRoutine.updateCreateAt(LocalDateTime.parse("2021-10-19 21:07:55",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return waterRoutine;
    }

    public static List<Retrospect> makeWaterRetrospectList(Routine waterRoutine) {
        List<Retrospect> retrospectList = new ArrayList<>();
        Retrospect tueDay = Retrospect.builder().content("").routine(waterRoutine).result(Result.DONE).build();
        tueDay.updateTestData(retrospectId++, LocalDate.parse("2021-10-19"));
        Retrospect thuDay = Retrospect.builder().content("").routine(waterRoutine).result(Result.TRY).build();
        thuDay.updateTestData(retrospectId++, LocalDate.parse("2021-10-21"));
        Retrospect satDay = Retrospect.builder().content("").routine(waterRoutine).result(Result.DONE).build();
        satDay.updateTestData(retrospectId++, LocalDate.parse("2021-10-23"));
        retrospectList.add(tueDay); retrospectList.add(thuDay); retrospectList.add(satDay);
        return retrospectList;
    }

    public static Routine makeVitaminRoutine(Account account) {
        List<Week> vitaminDays = new ArrayList<>();
        vitaminDays.add(Week.MON); vitaminDays.add(Week.TUE); vitaminDays.add(Week.WED); vitaminDays.add(Week.THU);
        vitaminDays.add(Week.FRI); vitaminDays.add(Week.SAT); vitaminDays.add(Week.SUN);
        RoutineDTO.RequestRoutineDto newRoutine1 = new RoutineDTO.RequestRoutineDto("비타민 먹기", "비타민 챙겨 먹기", vitaminDays, "07:35", "생활");
        Routine vitaminRoutine = Routine.builder().newRoutine(newRoutine1).id(routineId++).account(account).build();
        List<RoutineDay> routineDays = vitaminDays.stream().map(day -> RoutineDay.builder().day(day).routine(vitaminRoutine).build()).collect(Collectors.toList());
        vitaminRoutine.addDays(routineDays);
        vitaminRoutine.updateCreateAt(LocalDateTime.parse("2021-10-17 21:07:55",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return vitaminRoutine;
    }


}
