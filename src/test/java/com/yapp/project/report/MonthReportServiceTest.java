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
    @Transactional
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
    }
}
