package com.yapp.project.weekReport.domain;

import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.routine.domain.Week;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor
public class RetrospectResultDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoutineResult routineResult;

    private String day;

    @Enumerated(EnumType.STRING)
    private Result result;

    @Builder
    public RetrospectResultDay(String day, Result result, RoutineResult routineResult) {
        this.day = day;
        this.result = result;
        this.routineResult = routineResult;
        routineResult.addRetrospectDay(this);
    }
}
