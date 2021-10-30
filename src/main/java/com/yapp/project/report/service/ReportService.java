package com.yapp.project.report.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.config.exception.report.AlreadyWeekReportFoundException;
import com.yapp.project.config.exception.report.MonthReportNotFoundMonthException;
import com.yapp.project.report.domain.dto.ReportDTO;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineRepository;
import com.yapp.project.report.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final RetrospectRepository retrospectRepository;
    private final RoutineRepository routineRepository;
    private final WeekReportRepository weekReportRepository;
    private final MonthRoutineReportRepository monthRoutineReportRepository;

    @Transactional(readOnly = true)
    public ReportDTO.ResponseRetrospectMessage getMonthReportByYearAndMonth(Account account, Integer year, Integer month) {
        List<MonthRoutineReport> monthReportList = monthRoutineReportRepository.findAllByAccountAndYearAndMonth(account, year, month);
        if(monthReportList.size() == 0) {
            throw new MonthReportNotFoundMonthException();
        }
        List<WeekReport> weekReportList =
                weekReportRepository.findAllByAccountAndMonthReportYearAndMonthReportMonthOrderByLastDate(account, year, month);
        List<String> weekRateList = weekReportList.stream().map(x -> x.getRate()).collect(Collectors.toList());
        return ReportDTO.ResponseRetrospectMessage.of(monthReportList, weekRateList);
    }

    @Transactional
    public void makeMonthReport(Account account) {
        List<WeekReport> weekReportList = weekReportRepository.findAllByAccountAndIsReportIsFalseOrderByLastDate(account);
        HashSet<Long> routineIdHashSet = new HashSet();
        List<RoutineResult> routineResultList = getRoutineResultList(weekReportList, routineIdHashSet);
        List<MonthRoutineReport> monthRoutineReportList = getMonthRoutineReportList(account, routineIdHashSet);
        statisticsRoutineDuringMonth(routineResultList, monthRoutineReportList);
        weekReportList.forEach(x -> x.updateMonthReportYearAndMonth());
        weekReportRepository.saveAll(weekReportList);
        monthRoutineReportRepository.saveAll(monthRoutineReportList);
    }

    @Transactional
    public void makeWeekReport(Account account) {
        checkIsReported(account);
        /** index 0 : Total, 1 : fullyDone, 2 : particularlyDone */
        int result [] = new int[]{0, 0, 0};
        List<Routine> routineList = routineRepository.findAllByIsDeleteIsFalseAndAccount(account);
        routineList.stream().forEach(x -> result[0] += x.getDays().size());
        List<RoutineResult> routineResultList = getRoutineResults(result, routineList);
        List<Retrospect> retrospectList = retrospectRepository.findAllByIsReportIsFalseAndRoutineAccount(account);
        WeekReport weekReport = WeekReport.builder().build();
        statisticsRetrospect(result, routineResultList, retrospectList, weekReport);
        setRetrospectBasicData(account, result, weekReport);
        weekReportRepository.save(weekReport);
    }

    private void statisticsRoutineDuringMonth(List<RoutineResult> routineResultList, List<MonthRoutineReport> monthRoutineReportList) {
        monthRoutineReportList.forEach( x -> {
            routineResultList.forEach( y -> {
                if(x.getRoutineId() == y.getRoutineId()) {
                    x.updateMonthRoutineResultCount(y.getFullyDoneCount(), y.getPartiallyDoneCount(), y.getNotDoneCount());
                    x.updateRoutineTitleAndCategory(y.getTitle(), y.getCategory());
                }
            });
        });
    }

    private List<MonthRoutineReport> getMonthRoutineReportList(Account account, HashSet<Long> routineIdHashSet) {
        List<MonthRoutineReport> monthRoutineReportList = routineIdHashSet.stream().map(x ->
                MonthRoutineReport.builder().account(account).routineId(x).build()
        ).collect(Collectors.toList());
        return monthRoutineReportList;
    }

    private List<RoutineResult> getRoutineResultList(List<WeekReport> weekReportList, HashSet<Long> routineIdHashSet) {
        List<RoutineResult> routineResultList = new ArrayList<>();
        weekReportList.forEach(x -> {
            x.updateIsReport();
            x.getRoutineResults().forEach( y -> routineIdHashSet.add(y.getRoutineId()));
            routineResultList.addAll(x.getRoutineResults());
        });
        return routineResultList;
    }

    private void checkIsReported(Account account) {
        LocalDate lastMon = DateUtil.KST_LOCAL_DATE_NOW().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)); // 가장 최근 월요일
        Boolean isReported = weekReportRepository.existsByAccountAndLastDate(account, lastMon.minusDays(1));
        if(isReported){
            throw new AlreadyWeekReportFoundException();
        }
    }

    private void statisticsRetrospect(int[] result, List<RoutineResult> routineResultList, List<Retrospect> retrospectList, WeekReport weekReport) {
        LocalDate lastMon = DateUtil.KST_LOCAL_DATE_NOW().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)); // 가장 최근 월요일
        routineResultList.forEach(x -> {
            int tempResult[] = new int[] {0, 0}; /** index 0 : fullyDone, 1 : particularlyDone */
            retrospectList.forEach(y -> {
                if(y.getRoutine().getId() == x.getRoutineId() && y.getDate().isBefore(lastMon)) {
                    String day = y.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                    if(y.getResult() == Result.DONE){
                        tempResult[0]++;
                        result[1]++;
                        RetrospectReportDay.builder().routineResult(x).day(day).result(y.getResult()).build();
                    } else if(y.getResult() == Result.TRY){
                        tempResult[1]++;
                        result[2]++;
                        RetrospectReportDay.builder().routineResult(x).day(day).result(y.getResult()).build();
                    }
                    y.updateIsReport();
                }
            });
            x.addRoutineResultDoneCount(tempResult);
            x.addWeekReport(weekReport);
        });
    }

    private List<RoutineResult> getRoutineResults(int[] result, List<Routine> routineList) {
        LocalDate lastMon = DateUtil.KST_LOCAL_DATE_NOW().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)); // 가장 최근 월요일
        List<RoutineResult> routineResultList = routineList.stream().map(x -> {
            LocalDate startDate = x.getCreatedAt().toLocalDate();
            List<String> days = x.getDays().stream().map(y -> y.getDay().toString()).collect(Collectors.toList());
            int passDaysCount = 0;
            int allCount = days.size();
            if (startDate.plusDays(7).isAfter(lastMon)) {
                List<String> mockDays = new ArrayList<>();
                mockDays.addAll(days);
                List<String> newDays = new ArrayList<>();
                int i = 0;
                while (!startDate.plusDays(i).isEqual(lastMon)) {
                    newDays.add(startDate.plusDays(i++).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase());
                }
                mockDays.removeAll(newDays);
                passDaysCount = mockDays.size();
                allCount = days.size() - passDaysCount;
                result[0] -= mockDays.size();
            }
            RoutineResult routineResult = RoutineResult.builder().title(x.getTitle()).category(x.getCategory()).routineId(x.getId()).passDaysCount(passDaysCount).allCount(allCount).build();
            x.getDays().stream().map(y -> RoutineReportDay.builder().day(y.getDay()).routineResult(routineResult).build()).collect(Collectors.toList());
            return routineResult;
        }).collect(Collectors.toList());
        return routineResultList;
    }

    private void setRetrospectBasicData(Account account, int[] result, WeekReport weekReport) {
        weekReport.addBasicData(
                account,
                (int) (((result[1] + (result[2] * 0.5)) / result[0]) * 100) + "%",
                result[1],
                result[2],
                result[0] - (result[1] + result[2])
        );
    }

}