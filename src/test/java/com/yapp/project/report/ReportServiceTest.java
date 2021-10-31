package com.yapp.project.report;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.report.ReportTemplate;
import com.yapp.project.report.domain.WeekReport;
import com.yapp.project.report.domain.WeekReportRepository;
import com.yapp.project.report.domain.dto.ReportDTO;
import com.yapp.project.report.service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReportServiceTest {

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
        WeekReport week1Report = ReportTemplate.makeWeek1(savedAccount);
        WeekReport week2Report = ReportTemplate.makeWeek2(savedAccount);
        WeekReport week3Report = ReportTemplate.makeWeek3(savedAccount);
        WeekReport week4Report = ReportTemplate.makeWeek4(savedAccount);
        weekReportRepository.save(week1Report);
        weekReportRepository.save(week2Report);
        weekReportRepository.save(week3Report);
        weekReportRepository.save(week4Report);
        int year = DateUtil.KST_LOCAL_DATE_NOW().getYear();
        int month = DateUtil.KST_LOCAL_DATE_NOW().getMonth().getValue() - 1;

        // when
        reportService.makeMonthReport(savedAccount);
        ReportDTO.ResponseRetrospectMessage monthReportByYearAndMonth = reportService.getMonthReportByYearAndMonth(savedAccount, year, month);
        List<ReportDTO.ResponseMonthRoutineReport> data = monthReportByYearAndMonth.getData().getMonthRoutineReportList();

        // then
        assertThat(data.get(0).getFullyDoneRate()).isEqualTo("50%"); // Coffee - FullyDone
        assertThat(data.get(1).getFullyDoneRate()).isEqualTo("29%"); // Reading - FullyDone
        assertThat(data.get(2).getFullyDoneRate()).isEqualTo("44%"); // Running - FullyDone
        assertThat(data.get(3).getFullyDoneRate()).isEqualTo("52%"); // Water - FullyDone
        assertThat(data.get(4).getFullyDoneRate()).isEqualTo("0%");  // Vitamin - FullyDone
    }

}
