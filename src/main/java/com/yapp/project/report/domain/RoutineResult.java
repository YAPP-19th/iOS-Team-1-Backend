package com.yapp.project.report.domain;

import com.yapp.project.organization.domain.Category;
import com.yapp.project.routine.domain.RoutineCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class RoutineResult{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private WeekReport weekReport;

    @OneToMany(mappedBy = "routineResult", cascade = CascadeType.ALL)
    private List<RetrospectReportDay> retrospectReportDays = new ArrayList<>();

    private String title;

    @Enumerated(EnumType.STRING)
    private RoutineCategory category;

    private Long routineId;

    private Long fullyDoneCount;

    private Long partiallyDoneCount;

    private Long notDoneCount;

    private LocalDateTime routineCreateAt;

    @Builder
    public RoutineResult(String title, RoutineCategory category, Long routineId, LocalDateTime routineCreateAt) {
        this.title = title;
        this.category = category;
        this.routineId = routineId;
        this.routineCreateAt = routineCreateAt;
    }

    public void addRetrospectDay(RetrospectReportDay retrospectReportDay) {
        this.retrospectReportDays.add(retrospectReportDay);
    }

    public void addWeekReport(WeekReport weekReport) {
        this.weekReport = weekReport;
        weekReport.addRoutineResult(this);
    }

    public void addRoutineResultDoneCount(int[] routineResultCount) {
        this.fullyDoneCount = (long) routineResultCount[0];
        this.partiallyDoneCount = (long) routineResultCount[1];
    }

    public void addRoutineNotDoneCount(int notDoneCount) {
        this.notDoneCount = (long) notDoneCount;
    }
}