package com.yapp.project.routine.domain;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoutineDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestRoutineDto {
        @ApiModelProperty(value = "타이틀", example = "단어 외우기", required = true)
        private String title;

        @ApiModelProperty(value = "목표", example = "티끌 모아 태산! 오늘부터 시작해보는건 어때요?", required = true)
        private String goal;

        @ApiModelProperty(value = "하는 요일", example = "['MON', 'SUN']", required = true)
        private List<Week> days = new ArrayList<>();

        @ApiModelProperty(value = "하는 시간", example = "07:35", required = true)
        private String startTime;

        @ApiModelProperty(value = "카테고리", example = "생활", required = true)
        private String category;
    }

    @Getter
    @Setter
    public static class ResponseRoutineDto {
        @ApiModelProperty(value = "루틴ID", example = "1")
        private Long id;

        @ApiModelProperty(value = "타이틀", example = "단어 외우기")
        private String title;

        @ApiModelProperty(value = "목표", example = "티끌 모아 태산! 오늘부터 시작해보는건 어때요?")
        private String goal;

        @ApiModelProperty(value = "하는 요일", example = "['MON', 'SUN']")
        private List<Week> days = new ArrayList<>();

        @ApiModelProperty(value = "하는 시간", example = "07:35")
        private String startTime;

        @ApiModelProperty(value = "카테고리", example = "생활")
        private String category;

        @Builder
        public ResponseRoutineDto(Routine routine) {
            this.id = routine.getId();
            this.title = routine.getTitle();
            this.goal = routine.getGoal();
            this.startTime = routine.getStartTime().toString();
            this.days = routine.getDays().stream().map(day -> day.getDay()).collect(Collectors.toList());
            this.category = routine.getCategory();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ResponseRoutineMessageDto {
        private Message message;
        private ResponseRoutineDto data;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ResponseRoutineListMessageDto {
        private Message message;
        private List<ResponseRoutineDto> data;
    }
}