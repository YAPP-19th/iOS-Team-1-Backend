package com.yapp.project.weekReport.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.config.exception.weekReport.AlreadyWeekReportFoundException;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineRepository;
import com.yapp.project.weekReport.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeekReportService {

    private final RetrospectRepository retrospectRepository;
    private final RoutineRepository routineRepository;
    private final WeekReportRepository weekReportRepository;

    @Transactional
    public void mackReport(Account account) {
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

    private void checkIsReported(Account account) {
        LocalDate lastMon = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)); // 가장 최근 월요일
        Boolean isReported = weekReportRepository.existsByAccountAndLastDate(account, lastMon.minusDays(1));
        if(isReported){
            throw new AlreadyWeekReportFoundException();
        }
    }

    private void statisticsRetrospect(int[] result, List<RoutineResult> routineResultList, List<Retrospect> retrospectList, WeekReport weekReport) {
        LocalDate lastMon = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)); // 가장 최근 월요일
        routineResultList.forEach(x -> {
            retrospectList.forEach(y -> {
                if(y.getRoutine().getId() == x.getRoutineId() && y.getDate().isBefore(lastMon)) {
                    String day = y.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                    if(y.getResult() == Result.DONE){
                        result[1]++;
                        RetrospectReportDay.builder().routineResult(x).day(day).result(y.getResult()).build();
                    } else if(y.getResult() == Result.TRY){
                        result[2]++;
                        RetrospectReportDay.builder().routineResult(x).day(day).result(y.getResult()).build();
                    }
                    y.updateIsReport(); // TODO OPen
                }
            });
            x.addWeekReport(weekReport);
        });
    }

    private List<RoutineResult> getRoutineResults(int[] result, List<Routine> routineList) {
        LocalDate lastMon = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)); // 가장 최근 월요일
        List<RoutineResult> routineResultList = routineList.stream().map(x -> {
            LocalDate startDate = x.getCreatedAt().toLocalDate();
            List<String> days = x.getDays().stream().map(y -> y.getDay().toString()).collect(Collectors.toList());
            if (startDate.plusDays(7).isAfter(lastMon)) {
                List<String> mockDays = new ArrayList<>();
                mockDays.addAll(days);
                List<String> newDays = new ArrayList<>();
                int i = 0;
                while (!startDate.plusDays(i).isEqual(lastMon)) {
                    newDays.add(startDate.plusDays(i++).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase());
                }
                mockDays.removeAll(newDays);
                result[0] -= mockDays.size();
            }
            RoutineResult routineResult = RoutineResult.builder().title(x.getTitle()).category(x.getCategory()).routineId(x.getId()).build();
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