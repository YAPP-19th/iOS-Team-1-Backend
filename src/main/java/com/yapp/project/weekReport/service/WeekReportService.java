package com.yapp.project.weekReport.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineRepository;
import com.yapp.project.weekReport.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public WeekReport test222(Account account) {

        Long result [] = new Long[]{0L, 0L, 0L};
//        int all = 0
//        int fullyDone = 0;
//        int partiallyDone = 0;

        LocalDate lastMon = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)); // 가장 최근 월요일

        // 삭제상태가 아닌 루틴 전체
        List<Routine> routineList = routineRepository.findAllByIsDeleteIsFalseAndAccount(account);
        routineList.stream().forEach(x -> result[0] += x.getDays().size());

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
            RoutineResult routineResult = RoutineResult.builder()
                    .title(x.getTitle())
                    .category(x.getCategory())
                    .routineId(x.getId()).build();
            x.getDays().stream().map(y -> RoutineReportDay.builder().day(y.getDay()).routineResult(routineResult).build()).collect(Collectors.toList());
            return routineResult;
        }).collect(Collectors.toList());

        // 리포트 되지 않은 회고 전체
        List<Retrospect> retrospectList = retrospectRepository.findAllByIsReportIsFalseAndRoutineAccount(account);

        WeekReport weekReport = WeekReport.builder()
                .mon(lastMon)
                .build();

        routineResultList.forEach( x -> {
            retrospectList.forEach( y -> {
                if(y.getRoutine().getId() == x.getRoutineId() && y.getDate().isBefore(lastMon)) {
                    String day = y.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                    if(y.getResult() == Result.DONE){
                        result[1]++;
                        RetrospectResultDay.builder().routineResult(x).day(day).result(y.getResult()).build();
//                        x.addRetrospectDay(RetrospectResultDay.builder().day(day).result(y.getResult()).build());
                    } else if(y.getResult() == Result.TRY){
                        result[2]++;
                        RetrospectResultDay.builder().routineResult(x).day(day).result(y.getResult()).build();
//                        x.addRetrospectDay(RetrospectResultDay.builder().day(day).result(y.getResult()).build());
                    }
//                    y.updateIsReport(); // TODO OPen
                }
            });
            x.addWeekReport(weekReport);
        });
        weekReport.updateBasicData(
                account,
                (int) (((result[1] + (result[2] * 0.5)) / result[0]) * 100) + "%",
                result[1],
                result[2],
                result[0] - (result[1] + result[2])
                );
        return weekReportRepository.save(weekReport);
    }
}