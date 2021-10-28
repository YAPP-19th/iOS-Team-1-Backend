package com.yapp.project.weekReport.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class RoutineResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private WeekReport weekReport;

    @OneToMany(mappedBy = "routineResult")
    private List<RoutineResultDay> routineResultDayList;

    private String title;

    private String category;
}
