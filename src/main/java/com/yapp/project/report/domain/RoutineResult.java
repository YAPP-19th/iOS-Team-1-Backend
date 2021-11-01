package com.yapp.project.report.domain;

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

    private String category;

    private Long routineId;

    private Long allCount;

    private Long fullyDoneCount;

    private Long partiallyDoneCount;

    private Long notDoneCount;

    private Long passDaysCount;

    private LocalDateTime routineCreateAt;

    @Builder
    public RoutineResult(String title, String category, Long routineId, int passDaysCount, int allCount, LocalDateTime routineCreateAt) {
        this.title = title;
        this.category = category;
        this.routineId = routineId;
        this.passDaysCount = Long.valueOf(passDaysCount);
        this.allCount = Long.valueOf(allCount);
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
        this.fullyDoneCount = Long.valueOf(routineResultCount[0]);
        this.partiallyDoneCount = Long.valueOf(routineResultCount[1]);
        this.notDoneCount = allCount - (fullyDoneCount + partiallyDoneCount);
    }
}