package com.yapp.project.weekReport.domain;

import com.yapp.project.account.domain.Account;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class WeekReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @OneToMany(mappedBy = "weekReport")
    private List<RoutineResult> routineResults;

    private Double rate;

    private Long fullyDone;

    private Long partiallyDone;

    private Long notDone;

    private LocalDate lastDate;
}
