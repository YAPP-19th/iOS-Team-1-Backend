package com.yapp.project.report.domain;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.common.DateUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;

@Entity
@Getter
@NoArgsConstructor
public class MonthRoutineReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    private String title;

    private Long routineId;

    private String category;

    private Long fullyDoneCount;

    private Long partiallyDoneCount;

    private Long notDoneCount;

    private Integer year;

    private Integer month;

    @Builder
    public MonthRoutineReport(Account account, Long routineId) {
        this.account = account;
        this.routineId = routineId;
        this.year = DateUtil.KST_LOCAL_DATE_NOW().getYear();
        this.month = DateUtil.KST_LOCAL_DATE_NOW().getMonth().getValue() - 1;
        this.fullyDoneCount = 0L;
        this.partiallyDoneCount = 0L;
        this.notDoneCount = 0L;
    }

    public void updateMonthRoutineResultCount(Long fullyDoneCount, Long partiallyDoneCount, Long notDoneCount) {
        this.fullyDoneCount += fullyDoneCount;
        this.partiallyDoneCount += partiallyDoneCount;
        this.notDoneCount += notDoneCount;
    }

    public void updateRoutineTitleAndCategory(String title, String category) {
        this.title = title;
        this.category = category;
    }

    public void deleteReport() {
        this.account = null;
    }
}
