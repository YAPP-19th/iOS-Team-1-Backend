package com.yapp.project.weekReport.domain.dto;

import com.yapp.project.aux.Message;

import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.routine.domain.RoutineDay;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class WeekReportDTO {

    @Getter
    @Setter
    public static class ReportRoutineDTO {

        private Long routineId;

        private String title;

        private List<String> days = new ArrayList<>();

        private String category;

        private List<RetrospectResult> retrospectResultList = new ArrayList<>();

        @Builder
        public ReportRoutineDTO(Long routineId, String title, List<String> days, String category) {
            this.routineId = routineId;
            this.title = title;
            this.days.addAll(days);
            this.category = category;

        }

        public void addRetrospectDay(RetrospectResult retrospectDay) {
            this.retrospectResultList.add(retrospectDay);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ResponseTest {
        private Message message;
        private List<ReportRoutineDTO> data;
        private Integer fullyDone;
        private Integer partiallyDone;
        private Integer notDone;
        private String rate;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class RetrospectResult {
        private String day;
        private Result result;
    }
}
