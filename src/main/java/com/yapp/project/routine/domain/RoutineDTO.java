package com.yapp.project.routine.domain;

import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class RoutineDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {
        private String title;
        private String goal;
        private List<Week> days = new ArrayList<>();
        private String startTime;
        private String category;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String goal;
        private LocalTime startTime;
        private List<Week> days = new ArrayList<>();
        private String category;

        @Builder
        public Response(Routine routine) {
            this.id = routine.getId();
            this.title = routine.getTitle();
            this.goal = routine.getGoal();
            this.startTime = routine.getStartTime();
            this.days = routine.getCrons().stream().map(cron -> cron.getWeek()).collect(Collectors.toList());
            this.category = routine.getCategory();
        }
    }

}
