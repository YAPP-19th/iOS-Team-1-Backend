package com.yapp.project.weekReport.domain;

import com.yapp.project.account.domain.Account;
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

    @OneToMany(mappedBy = "weekReport", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoutineResult> routineResults = new ArrayList<>();

    private String rate;

    private int fullyDone;

    private int partiallyDone;

    private int notDone;

    private LocalDate lastDate;

    @Builder
    public WeekReport() {
        this.lastDate = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)).minusDays(1); // 가장 최근 월요일
    }

    public void addBasicData(Account account, String rate, int fullyDone, int partiallyDone, int notDone) {
        this.account = account;
        this.rate = rate;
        this.fullyDone = fullyDone;
        this.partiallyDone = partiallyDone;
        this.notDone = notDone;
    }

    public void addRoutineResult(RoutineResult routineResult) {
        this.routineResults.add(routineResult);
    }
}
