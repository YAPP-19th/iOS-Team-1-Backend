package com.yapp.project.report;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.report.domain.WeekReport;
import com.yapp.project.report.domain.WeekReportRepository;
import com.yapp.project.report.service.ReportService;
import com.yapp.project.report.template.WeekReportTemplate;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class WeekReportServiceTest {

    @InjectMocks
    private ReportService reportService;

    @Mock
    private RoutineRepository routineRepository;
    @Mock
    private RetrospectRepository retrospectRepository;
    @Mock
    private WeekReportRepository weekReportRepository;

    @Test
    void testMakeWeekReportCase1() {
        try(MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)) {
            // given
            Account account = AccountTemplate.makeTestAccount();
            List<Routine> routineList = WeekReportTemplate.makeWeek1Routine(account);
            List<Retrospect> retrospectList = WeekReportTemplate.makeWeek1Retrospect(routineList);
            // mocking
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-10-06"));
            given(routineRepository.findAllByIsDeleteIsFalseAndAccount(account)).willReturn(routineList);
            given(retrospectRepository.findAllByIsReportIsFalseAndRoutineAccount(account)).willReturn(retrospectList);
            given(weekReportRepository.existsByAccountAndLastDate(account, LocalDate.parse("2021-10-03"))).willReturn(false);
            // when
            WeekReport weekReport = reportService.makeWeekReport(account);
            // then
            assertThat(weekReport.getRate()).isEqualTo(62);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testMakeWeekReportCase2() {
        try(MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)) {
            // given
            Account account = AccountTemplate.makeTestAccount();
            List<Routine> routineList = WeekReportTemplate.makeWeek2Routine(account);
            List<Retrospect> retrospectList = WeekReportTemplate.makeWeek2Retrospect(routineList);
            // mocking
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-10-13"));
            given(routineRepository.findAllByIsDeleteIsFalseAndAccount(account)).willReturn(routineList);
            given(retrospectRepository.findAllByIsReportIsFalseAndRoutineAccount(account)).willReturn(retrospectList);
            given(weekReportRepository.existsByAccountAndLastDate(account, LocalDate.parse("2021-10-10"))).willReturn(false);
            // when
            WeekReport weekReport = reportService.makeWeekReport(account);
            // then
            assertThat(weekReport.getRate()).isEqualTo(66);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testMakeWeekReportCase3() {
        try(MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)) {
            // given
            Account account = AccountTemplate.makeTestAccount();
            List<Routine> routineList = WeekReportTemplate.makeWeek3Routine(account);
            List<Retrospect> retrospectList = WeekReportTemplate.makeWeek3Retrospect(routineList);
            // mocking
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-10-20"));
            given(routineRepository.findAllByIsDeleteIsFalseAndAccount(account)).willReturn(routineList);
            given(retrospectRepository.findAllByIsReportIsFalseAndRoutineAccount(account)).willReturn(retrospectList);
            given(weekReportRepository.existsByAccountAndLastDate(account, LocalDate.parse("2021-10-17"))).willReturn(false);
            // when
            WeekReport weekReport = reportService.makeWeekReport(account);
            // then
            assertThat(weekReport.getRate()).isEqualTo(69);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testMakeWeekReportCase4() {
        try(MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)) {
            // given
            Account account = AccountTemplate.makeTestAccount();
            List<Routine> routineList = WeekReportTemplate.makeWeek4Routine(account);
            List<Retrospect> retrospectList = WeekReportTemplate.makeWeek4Retrospect(routineList);
            // mocking
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-10-27"));
            given(routineRepository.findAllByIsDeleteIsFalseAndAccount(account)).willReturn(routineList);
            given(retrospectRepository.findAllByIsReportIsFalseAndRoutineAccount(account)).willReturn(retrospectList);
            given(weekReportRepository.existsByAccountAndLastDate(account, LocalDate.parse("2021-10-24"))).willReturn(false);
            // when
            WeekReport weekReport = reportService.makeWeekReport(account);
            // then
            assertThat(weekReport.getRate()).isEqualTo(63);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
