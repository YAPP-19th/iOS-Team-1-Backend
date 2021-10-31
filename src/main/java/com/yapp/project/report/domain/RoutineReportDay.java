package com.yapp.project.report.domain;

import com.yapp.project.routine.domain.Week;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class RoutineReportDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoutineResult routineResult;

    @Enumerated(EnumType.STRING)
    private Week day;

    @Builder
    public RoutineReportDay(Week day, RoutineResult routineResult) {
        this.day = day;
        this.routineResult = routineResult;
        routineResult.addRoutineReportDay(this);
    }
}
