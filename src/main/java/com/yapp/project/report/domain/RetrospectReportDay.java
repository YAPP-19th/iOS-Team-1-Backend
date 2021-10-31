package com.yapp.project.report.domain;

import com.yapp.project.retrospect.domain.Result;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RetrospectReportDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoutineResult routineResult;

    private String day;

    @Enumerated(EnumType.STRING)
    private Result result;

    @Builder
    public RetrospectReportDay(String day, Result result, RoutineResult routineResult) {
        this.day = day;
        this.result = result;
        this.routineResult = routineResult;
        routineResult.addRetrospectDay(this);
    }
}
