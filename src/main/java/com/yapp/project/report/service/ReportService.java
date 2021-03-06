package com.yapp.project.report.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.config.exception.report.AlreadyWeekReportFoundException;
import com.yapp.project.config.exception.report.MonthReportNotFoundMonthException;
import com.yapp.project.config.exception.report.WeekReportNotFoundMonthException;
import com.yapp.project.report.domain.dto.ReportDTO;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineDay;
import com.yapp.project.routine.domain.RoutineRepository;
import com.yapp.project.report.domain.*;
import com.yapp.project.routine.domain.Week;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjuster;
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

    private static final DateFormat dateFormatW = new SimpleDateFormat("w");
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final int YEAR_OF_WEEK = 52;

    @Transactional
    public ReportDTO.ResponseWeekReportMessage getWeekReportLastDate(Account account, LocalDate date) {
        WeekReport weekReport = weekReportRepository.findByAccountAndLastDate(account, date).orElseThrow(WeekReportNotFoundMonthException::new);
        return ReportDTO.ResponseWeekReportMessage.of(weekReport);
    }

    @Transactional(readOnly = true)
    public ReportDTO.ResponseMonthReportMessage getMonthReportByYearAndMonth(Account account, Integer year, Integer month) throws ParseException {
        List<MonthRoutineReport> monthReportList = monthRoutineReportRepository.findAllByAccountAndYearAndMonth(account, year, month);
        List<WeekReport> weekReportList =
                weekReportRepository.findAllByAccountAndMonthReportYearAndMonthReportMonthOrderByLastDate(account, year, month);
        if(monthReportList.isEmpty() && weekReportList.isEmpty()) {
            throw new MonthReportNotFoundMonthException();
        }
        LocalDate tempDate = LocalDate.of(year, month, 1);
        List<Integer> weekRateList = List.of(getWeekRateList(weekReportList, tempDate));
        return ReportDTO.ResponseMonthReportMessage.of(monthReportList, weekRateList);
    }

    @Transactional
    public void makeMonthReport(Account account) {
        List<WeekReport> weekReportList = weekReportRepository.findAllByAccountAndIsReportIsFalseOrderByLastDate(account);
        HashSet<Long> routineIdHashSet = new HashSet<>();
        List<RoutineResult> routineResultList = getRoutineResultList(weekReportList, routineIdHashSet);
        List<MonthRoutineReport> monthRoutineReportList = getMonthRoutineReportList(account, routineIdHashSet);
        statisticsRoutineDuringMonth(routineResultList, monthRoutineReportList);
        weekReportList.forEach(WeekReport::updateMonthReportYearAndMonth);
        weekReportRepository.saveAll(weekReportList);
        monthRoutineReportRepository.saveAll(monthRoutineReportList);
    }

    @Transactional
    public WeekReport makeWeekReport(Account account) throws ParseException {
        checkIsReported(account);
        /** index 0 : notDone, 1 : fullyDone, 2 : particularlyDone */
        int[] result = new int[]{0, 0, 0};
        LocalDate LAST_MON = DateUtil.KST_LOCAL_DATE_NOW().with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        List<Routine> routineList = routineRepository.findAllByIsDeleteIsFalseAndAccount(account)
                .stream().filter(routine -> LocalDate.from(routine.getCreatedAt())
                        .isBefore(LAST_MON)).collect(Collectors.toList());
        List<RoutineResult> routineResultList = getRoutineResults( routineList);
        List<Retrospect> retrospectList = retrospectRepository.findAllByIsReportIsFalseAndRoutineAccount(account);
        WeekReport weekReport = WeekReport.builder().build();
        statisticsRetrospect(result, routineResultList, retrospectList, weekReport);
        makeNotDoneRetrospectList(routineList, routineResultList, result);
        setRetrospectBasicData(account, result, weekReport);
        weekReportRepository.save(weekReport);
        return weekReport; // Test ?????? ?????? ?????? ?????? ??????.
    }

    private void statisticsRoutineDuringMonth(List<RoutineResult> routineResultList, List<MonthRoutineReport> monthRoutineReportList) {
        monthRoutineReportList.forEach( monthRoutineReport -> routineResultList.forEach(routineResult -> {
            if(Objects.equals(monthRoutineReport.getRoutineId(), routineResult.getRoutineId())) {
                monthRoutineReport.updateMonthRoutineResultCount(routineResult.getFullyDoneCount(), routineResult.getPartiallyDoneCount(), routineResult.getNotDoneCount());
                monthRoutineReport.updateRoutineTitleAndCategory(routineResult.getTitle(), routineResult.getCategory());
            }
        }));
    }

    private List<MonthRoutineReport> getMonthRoutineReportList(Account account, HashSet<Long> routineIdHashSet) {
        return routineIdHashSet.stream().map(routineId ->
                MonthRoutineReport.builder().account(account).routineId(routineId).build()
        ).collect(Collectors.toList());
    }

    private List<RoutineResult> getRoutineResultList(List<WeekReport> weekReportList, HashSet<Long> routineIdHashSet) {
        List<RoutineResult> routineResultList = new ArrayList<>();
        weekReportList.forEach(weekReport -> {
            weekReport.updateIsReport();
            weekReport.getRoutineResults().forEach( routineResult -> routineIdHashSet.add(routineResult.getRoutineId()));
            routineResultList.addAll(weekReport.getRoutineResults());
        });
        return routineResultList;
    }

    private void checkIsReported(Account account) {
        LocalDate LAST_MON = DateUtil.KST_LOCAL_DATE_NOW().with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        Boolean isReported = weekReportRepository.existsByAccountAndLastDate(account, LAST_MON.minusDays(1));
        if(isReported){
            throw new AlreadyWeekReportFoundException();
        }
    }

    private void statisticsRetrospect(int[] result, List<RoutineResult> routineResultList, List<Retrospect> retrospectList, WeekReport weekReport) {
        LocalDate LAST_MON = DateUtil.KST_LOCAL_DATE_NOW().with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        routineResultList.forEach(routineResult -> {
            int[] tempResult = new int[] {0, 0}; /** index 0 : fullyDone, 1 : particularlyDone */
            retrospectList.forEach(retrospect -> {
                if(Objects.equals(retrospect.getRoutine().getId(), routineResult.getRoutineId()) && retrospect.getDate().isBefore(LAST_MON)) {
                    String day = retrospect.getDate().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
                    if(retrospect.getResult() == Result.DONE){
                        tempResult[0]++;
                        result[1]++;
                        RetrospectReportDay.builder().routineResult(routineResult).day(day).result(retrospect.getResult()).build();
                    } else if(retrospect.getResult() == Result.TRY){
                        tempResult[1]++;
                        result[2]++;
                        RetrospectReportDay.builder().routineResult(routineResult).day(day).result(retrospect.getResult()).build();
                    }
                    retrospect.updateIsReportTrue();
                }
            });
            routineResult.addRoutineResultDoneCount(tempResult);
            routineResult.addWeekReport(weekReport);
        });
    }

    private void makeNotDoneRetrospectList(List<Routine> routineList, List<RoutineResult> routineResultList, int[] result) {
        LocalDate LAST_MON = DateUtil.KST_LOCAL_DATE_NOW().with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        routineResultList.forEach(routineResult -> {
            List<Week> retrospectDayList = routineResult.getRetrospectReportDays().stream().map( day -> Week.valueOf(day.getDay())).collect(Collectors.toList());
            LocalDate startDate = routineResult.getRoutineCreateAt().toLocalDate();
            List<Week> notDays = new ArrayList<>();
            for (Routine routine: routineList) {
                List<Week> routineDays = routine.getDays().stream().map(RoutineDay::getDay).collect(Collectors.toList());
                if (Objects.equals(routine.getId(), routineResult.getRoutineId())) {
                    if (startDate.plusDays(7).isAfter(LAST_MON)) {
                        List<Week> mockDays = new ArrayList<>();
                        notDays.addAll(routineDays);
                        int i = 0;
                        while (!startDate.plusDays(i).isEqual(LAST_MON)) {
                            mockDays.add(Week.valueOf(startDate.plusDays(i++).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase()));
                        }
                        routineDays.removeAll(mockDays);
                        notDays.removeAll(routineDays);
                    } else {
                        notDays.addAll(routine.getDays().stream().map(RoutineDay::getDay).collect(Collectors.toList()));
                    }
                    break;
                }
            }
            notDays.removeAll(retrospectDayList);
            result[0] += notDays.size();
            notDays.forEach( newDay ->
                    RetrospectReportDay.builder().routineResult(routineResult).day(newDay.getWeek()).result(Result.NOT).build());
            routineResult.addRoutineNotDoneCount(notDays.size());
        });

    }

    private List<RoutineResult> getRoutineResults(List<Routine> routineList) {
        return routineList.stream().map(routine -> RoutineResult.builder()
                    .title(routine.getTitle()).category(routine.getCategory()).routineId(routine.getId())
                    .routineCreateAt(routine.getCreatedAt()).build()).collect(Collectors.toList());
    }

    private void setRetrospectBasicData(Account account, int[] result, WeekReport weekReport) throws ParseException {
        String weekNum = dateFormatW.format(dateFormat.parse(String.valueOf(DateUtil.KST_LOCAL_DATE_NOW())));
        weekReport.addBasicData(
                account,
                (int) (((result[1] + (result[2] * 0.5)) / (result[0] + (result[1] + result[2]))) * 100),
                result[0],
                result[1],
                result[2],
                Integer.parseInt(weekNum)
        );
    }

    private Integer[] getWeekRateList(List<WeekReport> weekReportList, LocalDate tempDate) throws ParseException {
        Integer[] weekRateList = new Integer[] {0, 0, 0, 0, 0};
        Integer lastWeekNum = getLastWeekNum(tempDate);
        weekReportList.forEach( weekReport -> {
            int index = (weekReport.getWeekNum() - lastWeekNum) - 1;
            if(index < 0) {
                index = (( YEAR_OF_WEEK +  weekReport.getWeekNum()) - lastWeekNum - 1);
            }
            weekRateList[index] = weekReport.getRate();
        });
        return weekRateList;
    }

    private Integer getLastWeekNum(LocalDate tempDate) throws ParseException {
        TemporalAdjuster temporalAdjuster = TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.WEDNESDAY);
        LocalDate date = tempDate.with(temporalAdjuster);
        String lastWeekStr = dateFormatW.format(dateFormat.parse(String.valueOf(date)));
        return Integer.parseInt(lastWeekStr);
    }
}