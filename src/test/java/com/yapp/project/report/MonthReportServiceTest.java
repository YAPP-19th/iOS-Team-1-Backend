package com.yapp.project.report;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.report.domain.MonthRoutineReport;
import com.yapp.project.report.domain.WeekReport;
import com.yapp.project.report.domain.WeekReportRepository;
import com.yapp.project.report.domain.dto.ReportDTO;
import com.yapp.project.report.service.ReportService;
import com.yapp.project.report.template.MonthReportTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MonthReportServiceTest {

    @Autowired
    private WeekReportRepository weekReportRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ReportService reportService;

    @Test
    @Transactional
    void testMonthReportSuccess(){
        try(MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)) {
            //given
            Account account = AccountTemplate.makeTestAccount();
            Account savedAccount = accountRepository.save(account);
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-10-06"));
            WeekReport week1Report = MonthReportTemplate.makeWeek1(savedAccount);
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-10-13"));
            WeekReport week2Report = MonthReportTemplate.makeWeek2(savedAccount);
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-10-20"));
            WeekReport week3Report = MonthReportTemplate.makeWeek3(savedAccount);
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-10-27"));
            WeekReport week4Report = MonthReportTemplate.makeWeek4(savedAccount);
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-11-03"));
            weekReportRepository.save(week1Report);
            weekReportRepository.save(week2Report);
            weekReportRepository.save(week3Report);
            weekReportRepository.save(week4Report);
            int year = DateUtil.KST_LOCAL_DATE_NOW().getYear();
            int month = DateUtil.KST_LOCAL_DATE_NOW().getMonth().getValue() - 1;

            // when
            reportService.makeMonthReport(savedAccount);
            ReportDTO.ResponseMonthReportMessage monthReportByYearAndMonth = reportService.getMonthReportByYearAndMonth(savedAccount, year, month);
            List<Integer> weekRateList = monthReportByYearAndMonth.getData().getWeekRateList();
            List<ReportDTO.MonthResultByCategory> resultByCategory = monthReportByYearAndMonth.getData().getResultByCategory();
            assertAll(
                    /* Check Week Rate */
                    () -> assertEquals(weekRateList.get(0), 45),
                    () -> assertEquals(weekRateList.get(1), 30),
                    () -> assertEquals(weekRateList.get(2), 42),
                    () -> assertEquals(weekRateList.get(3), 42),
                    /* Check Category Rate */
                    () -> assertEquals(resultByCategory.get(2).getRate(), 40),
                    () -> assertEquals(resultByCategory.get(3).getRate(), 60),
                    /* Check Routine Result(size) */
                    () -> assertEquals(resultByCategory.get(2).getRoutineReportList().get(0).getFullyDoneRate(), 44), // Running
                    () -> assertEquals(resultByCategory.get(2).getRoutineReportList().get(1).getFullyDoneRate(), 52), // Water
                    () -> assertEquals(resultByCategory.get(3).getRoutineReportList().get(0).getFullyDoneRate(), 50), // Coffee
                    () -> assertEquals(resultByCategory.get(3).getRoutineReportList().get(1).getFullyDoneRate(), 29), // Reading
                    () -> assertEquals(resultByCategory.get(3).getRoutineReportList().get(2).getFullyDoneRate(), 0) // Vitamin
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @DisplayName("12월 월리포트 발급되는 케이스")
    void makeMonthReportWhenLastMonth() {
        try(MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)) {
            Account account = AccountTemplate.makeTestAccount();
            Account savedAccount = accountRepository.save(account);
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2022-01-05"));
            MonthRoutineReport monthRoutineReport = MonthRoutineReport.builder().account(savedAccount).routineId(1L).build();
            assertAll(
                    () -> assertEquals(2021, monthRoutineReport.getYear()),
                    () -> assertEquals(12, monthRoutineReport.getMonth())
            );
        }
    }
}