package com.yapp.project.report.domain;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.common.DateUtil;
import lombok.Builder;
import lombok.Getter;
import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class WeekReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "weekReport", cascade = CascadeType.ALL)
    private List<RoutineResult> routineResults = new ArrayList<>();

    private String rate;

    private int fullyDoneCount;

    private int partiallyDoneCount;

    private int notDoneCount;

    private LocalDate lastDate;

    private Boolean isReport;

    private Integer monthReportYear;

    private Integer monthReportMonth;

    @Builder
    public WeekReport() {
        this.lastDate = DateUtil.KST_LOCAL_DATE_NOW().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)).minusDays(1); // 가장 최근 월요일
        this.isReport = false;
    }

    public void addBasicData(Account account, String rate, int fullyDoneCount, int partiallyDoneCount, int notDoneCount) {
        this.account = account;
        this.rate = rate;
        this.fullyDoneCount = fullyDoneCount;
        this.partiallyDoneCount = partiallyDoneCount;
        this.notDoneCount = notDoneCount;
    }

    public void addRoutineResult(RoutineResult routineResult) {
        this.routineResults.add(routineResult);
    }

    public void updateIsReport() {
        this.isReport = true;
    }

    public void updateMonthReportYearAndMonth() {
        this.monthReportYear = DateUtil.KST_LOCAL_DATE_NOW().getYear();
        this.monthReportMonth = DateUtil.KST_LOCAL_DATE_NOW().getMonth().getValue() - 1;
    }

    public void deleteReport() {
        this.account = null;
    }

    /** Test용 */
    public void addIdAndLastDate(Long id, String lastDate) {
        this.id = id;
        this.lastDate = LocalDate.parse(lastDate);
    }
}
