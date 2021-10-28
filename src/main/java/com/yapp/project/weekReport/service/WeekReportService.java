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

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

        // 삭제상태가 아닌 루틴 전체
        List<Routine> routineList = routineRepository.findAllByIsDeleteIsFalseAndAccount(account);
        routineList.stream().forEach(x -> result[0] += x.getDays().size());

        List<WeekReportDTO.ReportRoutineDTO> reportRoutineDTOList = routineList.stream().map(x ->
                WeekReportDTO.ReportRoutineDTO.builder()
                        .routineId(x.getId()).
                        title(x.getTitle())
                        .days(x.getDays().stream().map(y -> y.getDay().toString()).collect(Collectors.toList()))
                        .category(x.getCategory())
                        .build()).collect(Collectors.toList());

        // 리포트 되지 않은 회고 전체
        List<Retrospect> retrospectList = retrospectRepository.findAllByIsReportIsFalseAndRoutineAccount(account);



        reportRoutineDTOList.forEach( x -> {
            retrospectList.forEach( y -> {
                if(y.getRoutine().getId() == x.getRoutineId()) {
                    LocalDate monDay = LocalDate.now().minusDays(2); // 수요일에서 2일 뒤 이전 즉, 월요일
                    if(y.getDate().isBefore(monDay)) {
                        String day = y.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                        x.addRetrospectDay(day);
                        if(y.getResult() == Result.DONE)
                            result[1]++;
                        else if(y.getResult() == Result.TRY)
                            result[2]++;
                    }
                }
            });
        });


        return WeekReportDTO.ResponseTest.builder()
                .data(reportRoutineDTOList)
                .fullyDone(result[1])
                .partiallyDone(result[2])
                .notDone(result[0] - (result[1] + result[2]))
                .build();
    }
}
