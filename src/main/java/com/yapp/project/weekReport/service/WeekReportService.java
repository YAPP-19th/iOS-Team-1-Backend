package com.yapp.project.weekReport.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineRepository;
import com.yapp.project.weekReport.domain.dto.WeekReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeekReportService {

    private final RetrospectRepository retrospectRepository;
    private final RoutineRepository routineRepository;

    public WeekReportDTO.ResponseTest makeWeekReport(Account account) {

        int result [] = new int[]{0, 0, 0};
//        int all = 0
//        int fullyDone = 0;
//        int partiallyDone = 0;

        LocalDate lastMon = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)); // 가장 최근 월요일

        // 삭제상태가 아닌 루틴 전체
        List<Routine> routineList = routineRepository.findAllByIsDeleteIsFalseAndAccount(account);
        routineList.stream().forEach(x -> result[0] += x.getDays().size());

        List<WeekReportDTO.ReportRoutineDTO> reportRoutineDTOList = routineList.stream().map(x -> {
            LocalDate startDate = x.getCreatedAt().toLocalDate();
            List<String> days = x.getDays().stream().map(y -> y.getDay().toString()).collect(Collectors.toList());
            if (startDate.plusDays(7).isAfter(lastMon)) {
                List<String> mockDays = new ArrayList<>();
                mockDays.addAll(days);
                List<String> newDays = new ArrayList<>();
                int i = 0;
                while (!startDate.plusDays(i).isEqual(lastMon)) {
//                    System.out.println(startDate.plusDays(i).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase());
                    newDays.add(startDate.plusDays(i++).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase());
                }
                mockDays.forEach(z -> System.out.print(z + " "));
                System.out.println();
                newDays.forEach(z -> System.out.print(z + " "));
                System.out.println();
                mockDays.removeAll(newDays);
                mockDays.forEach(z -> System.out.print(z + " "));
                System.out.println("\n---");
                result[0] -= mockDays.size();
                System.out.println(result[0]);
            }
            return WeekReportDTO.ReportRoutineDTO.builder()
                    .routineId(x.getId()).
                    title(x.getTitle())
                    .days(days)
                    .category(x.getCategory())
                    .build();
        }).collect(Collectors.toList());

        // 리포트 되지 않은 회고 전체
        List<Retrospect> retrospectList = retrospectRepository.findAllByIsReportIsFalseAndRoutineAccount(account);

        reportRoutineDTOList.forEach( x -> {
            retrospectList.forEach( y -> {
                if(y.getRoutine().getId() == x.getRoutineId()) {
                    if(y.getDate().isBefore(lastMon)) {
                        String day = y.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                        if(y.getResult() == Result.DONE){
                            result[1]++;
                            x.addRetrospectDay(day);
                        }
                        else if(y.getResult() == Result.TRY){
                            result[2]++;
                            x.addRetrospectDay(day);
                        }
                    }
                }
            });
        });

        return WeekReportDTO.ResponseTest.builder()
                .data(reportRoutineDTOList)
                .fullyDone(result[1])
                .partiallyDone(result[2])
                .notDone(result[0] - (result[1] + result[2]))
                .rate(((result[1] + (result[2] * 0.5)) / result[0]) * 100 + "%")
                .build();
    }
}
