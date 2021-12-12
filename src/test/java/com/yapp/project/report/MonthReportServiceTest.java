package com.yapp.project.report;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.report.domain.WeekReport;
import com.yapp.project.report.domain.WeekReportRepository;
import com.yapp.project.report.domain.dto.ReportDTO;
import com.yapp.project.report.service.ReportService;
import com.yapp.project.report.template.MonthReportTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
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
//    @Transactional
    void testMonthReportSuccess() {
        //given
        Account account = AccountTemplate.makeTestAccount();
        Account savedAccount = accountRepository.save(account);
        WeekReport week1Report = MonthReportTemplate.makeWeek1(savedAccount);
        WeekReport week2Report = MonthReportTemplate.makeWeek2(savedAccount);
        WeekReport week3Report = MonthReportTemplate.makeWeek3(savedAccount);
        WeekReport week4Report = MonthReportTemplate.makeWeek4(savedAccount);
        weekReportRepository.save(week1Report);
        weekReportRepository.save(week2Report);
        weekReportRepository.save(week3Report);
        weekReportRepository.save(week4Report);
        int year = DateUtil.KST_LOCAL_DATE_NOW().getYear();
        int month = DateUtil.KST_LOCAL_DATE_NOW().getMonth().getValue() - 1;

        // when
        reportService.makeMonthReport(savedAccount);
        ReportDTO.ResponseMonthReportMessage monthReportByYearAndMonth = reportService.getMonthReportByYearAndMonth(savedAccount, year, month);
        ReportDTO.ResponseMonthReport data = monthReportByYearAndMonth.getData();
        List<ReportDTO.ResponseMonthRoutineReport> dailyRoutineList =
                monthReportByYearAndMonth.getData().getResultByCategory().get("daily").getRoutineReportList();
        List<ReportDTO.ResponseMonthRoutineReport> healthRoutineList =
                monthReportByYearAndMonth.getData().getResultByCategory().get("health").getRoutineReportList();

        assertAll(
                /* Check Week Rate */
                () -> assertEquals(data.getWeekRateList().get(0), "45%"),
                () -> assertEquals(data.getWeekRateList().get(1), "30%"),
                () -> assertEquals(data.getWeekRateList().get(2), "42%"),
                () -> assertEquals(data.getWeekRateList().get(3), "42%"),
                /* Check Category Rate */
                () -> assertEquals(data.getResultByCategory().get("daily").getRate(), 60),
                () -> assertEquals(data.getResultByCategory().get("health").getRate(), 40),
                /* Check Routine Result(size) */
                () -> assertEquals(dailyRoutineList.get(0).getFullyDoneRate(), "50%"), // Coffee
                () -> assertEquals(dailyRoutineList.get(1).getFullyDoneRate(), "29%"), // Reading
                () -> assertEquals(dailyRoutineList.get(2).getFullyDoneRate(), "0%"), // Vitamin
                () -> assertEquals(healthRoutineList.get(0).getFullyDoneRate(), "44%"), // Running
                () -> assertEquals(healthRoutineList.get(1).getFullyDoneRate(), "52%") // Water
        );
    }
}