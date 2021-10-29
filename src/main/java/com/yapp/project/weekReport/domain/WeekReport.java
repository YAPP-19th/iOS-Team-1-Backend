package com.yapp.project.weekReport.domain;

import com.yapp.project.account.domain.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class WeekReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "weekReport", cascade = CascadeType.ALL)
    private List<RoutineResult> routineResults = new ArrayList<>();

    private String rate;

    private Long fullyDone;

    private Long partiallyDone;

    private Long notDone;

    private LocalDate lastDate;

    @Builder
    public WeekReport( LocalDate mon) {
        this.lastDate = mon.minusDays(1);
    }

    public void updateBasicData(Account account, String rate, Long fullyDone, Long partiallyDone, Long notDone) {
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
