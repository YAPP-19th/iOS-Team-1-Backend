package com.yapp.project.weekReport.domain;

import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.routine.domain.Week;

import javax.persistence.*;

@Entity
public class RoutineResultDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoutineResult routineResult;

    private Week day;

    @Enumerated(EnumType.STRING)
    private Result result;
}
