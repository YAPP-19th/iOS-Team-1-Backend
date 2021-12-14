package com.yapp.project.report.template;

import com.yapp.project.account.domain.Account;
import com.yapp.project.organization.domain.Category;
import com.yapp.project.report.domain.RoutineResult;
import com.yapp.project.report.domain.WeekReport;
import com.yapp.project.routine.domain.RoutineCategory;

public class MonthReportTemplate {
    private MonthReportTemplate(){
    }
    private static Long id = 1L;

    public static WeekReport makeWeek1(Account account){
        WeekReport week1 = WeekReport.builder().build();
        week1.addIdAndLastDate(id++, "2021-10-10");
        week1.addBasicData(account, 45, 5, 2, 13);
        RoutineResult readBook = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(4L).title("독서").build();
        readBook.addWeekReport(week1); readBook.addRoutineResultDoneCount(new int[]{1, 0});
        readBook.addRoutineNotDoneCount(0);

        RoutineResult coffee = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(1L).title("커피").build();
        coffee.addWeekReport(week1); coffee.addRoutineResultDoneCount(new int[]{1, 0});
        coffee.addRoutineNotDoneCount(1);

        RoutineResult running = RoutineResult.builder().category(RoutineCategory.HEALTH).routineId(5L).title("달리기").build();
        running.addWeekReport(week1); running.addRoutineResultDoneCount(new int[]{1, 1});
        running.addRoutineNotDoneCount(2);

        RoutineResult water = RoutineResult.builder().category(RoutineCategory.HEALTH).routineId(6L).title("물마시기").build();
        water.addWeekReport(week1); water.addRoutineResultDoneCount(new int[]{2, 1});
        water.addRoutineNotDoneCount(3);

        RoutineResult vitamin = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(7L).title("비타민").build();
        vitamin.addWeekReport(week1); vitamin.addRoutineResultDoneCount(new int[]{0, 0});
        vitamin.addRoutineNotDoneCount(7);
        return week1;
    }

    public static WeekReport makeWeek2(Account account){
        WeekReport week2 = WeekReport.builder().build();
        week2.addIdAndLastDate(id++, "2021-10-17");
        week2.addBasicData(account, 30, 8, 2, 10);
        RoutineResult readBook = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(4L).title("독서").build();
        readBook.addWeekReport(week2); readBook.addRoutineResultDoneCount(new int[]{1, 0});
        readBook.addRoutineNotDoneCount(1);

        RoutineResult coffee = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(1L).title("커피").build();
        coffee.addWeekReport(week2); coffee.addRoutineResultDoneCount(new int[]{1, 1});
        coffee.addRoutineNotDoneCount(0);

        RoutineResult running = RoutineResult.builder().category(RoutineCategory.HEALTH).routineId(5L).title("달리기").build();
        running.addWeekReport(week2); running.addRoutineResultDoneCount(new int[]{2, 1});
        running.addRoutineNotDoneCount(1);

        RoutineResult water = RoutineResult.builder().category(RoutineCategory.HEALTH).routineId(6L).title("물마시기").build();
        water.addWeekReport(week2); water.addRoutineResultDoneCount(new int[]{4, 1});
        water.addRoutineNotDoneCount(2);

        RoutineResult vitamin = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(7L).title("비타민").build();
        vitamin.addWeekReport(week2); vitamin.addRoutineResultDoneCount(new int[]{0, 0});
        vitamin.addRoutineNotDoneCount(7);
        return week2;
    }

    public static WeekReport makeWeek3(Account account){
        WeekReport week3 = WeekReport.builder().build();
        week3.addIdAndLastDate(id++, "2021-10-24");
        week3.addBasicData(account, 42, 7, 3, 10);
        RoutineResult readBook = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(4L).title("독서").build();
        readBook.addWeekReport(week3); readBook.addRoutineResultDoneCount(new int[]{0, 1});
        readBook.addRoutineNotDoneCount(1);

        RoutineResult coffee = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(1L).title("커피").build();
        coffee.addWeekReport(week3); coffee.addRoutineResultDoneCount(new int[]{1, 1});
        coffee.addRoutineNotDoneCount(0);

        RoutineResult running = RoutineResult.builder().category(RoutineCategory.HEALTH).routineId(5L).title("달리기").build();
        running.addWeekReport(week3); running.addRoutineResultDoneCount(new int[]{2, 1});
        running.addRoutineNotDoneCount(1);

        RoutineResult water = RoutineResult.builder().category(RoutineCategory.HEALTH).routineId(6L).title("물마시기").build();
        water.addWeekReport(week3); water.addRoutineResultDoneCount(new int[]{4, 1});
        water.addRoutineNotDoneCount(2);

        RoutineResult vitamin = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(7L).title("비타민").build();
        vitamin.addWeekReport(week3); vitamin.addRoutineResultDoneCount(new int[]{0, 0});
        vitamin.addRoutineNotDoneCount(7);
        return week3;
    }

    public static WeekReport makeWeek4(Account account){
        WeekReport week4 = WeekReport.builder().build();
        week4.addIdAndLastDate(id++, "2021-10-31");
        week4.addBasicData(account, 42, 7, 3, 10);
        RoutineResult readBook = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(4L).title("독서").build();
        readBook.addWeekReport(week4); readBook.addRoutineResultDoneCount(new int[]{0, 1});
        readBook.addRoutineNotDoneCount(1);

        RoutineResult coffee = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(1L).title("커피").build();
        coffee.addWeekReport(week4); coffee.addRoutineResultDoneCount(new int[]{1, 1});
        coffee.addRoutineNotDoneCount(0);

        RoutineResult running = RoutineResult.builder().category(RoutineCategory.HEALTH).routineId(5L).title("달리기").build();
        running.addWeekReport(week4); running.addRoutineResultDoneCount(new int[]{2, 1});
        running.addRoutineNotDoneCount(1);

        RoutineResult water = RoutineResult.builder().category(RoutineCategory.HEALTH).routineId(6L).title("물마시기").build();
        water.addWeekReport(week4); water.addRoutineResultDoneCount(new int[]{4, 1});
        water.addRoutineNotDoneCount(2);

        RoutineResult vitamin = RoutineResult.builder().category(RoutineCategory.DAILY).routineId(7L).title("비타민").build();
        vitamin.addWeekReport(week4); vitamin.addRoutineResultDoneCount(new int[]{0, 0});
        vitamin.addRoutineNotDoneCount(7);
        return week4;
    }

}
