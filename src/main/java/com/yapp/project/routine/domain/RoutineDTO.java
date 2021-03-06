package com.yapp.project.routine.domain;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yapp.project.aux.content.RoutineContent.DAY_ROUTINE_RATE_OK;
import static com.yapp.project.aux.content.RoutineContent.RECOMMENDED_ROUTINE_OK;

public class RoutineDTO {
    private RoutineDTO() {}

    @Getter
    public static class ResponseRecommendedRoutine {
        @ApiModelProperty(value = "타이틀", example = "감사한 일 쓰기")
        private String title;
        @ApiModelProperty(value = "설명", example = "감사한 일, 소중한 대상을 떠올리며 적어보세요.")
        private String description;
        @ApiModelProperty(value = "카테고리", example = "0")
        private Integer category;

        @Builder
        public ResponseRecommendedRoutine(String title, String description, Integer category) {
            this.title = title;
            this.description = description;
            this.category = category;
        }
    }

    @Getter
    public static class ResponseRoutineDaysRate {
        @ApiModelProperty(value = "요일", example = "2021-10-18")
        private LocalDate date;
        @ApiModelProperty(value = "성공", example = "1")
        private Integer fullyDone;
        @ApiModelProperty(value = "부분성공", example = "0")
        private Integer partiallyDone;
        @ApiModelProperty(value = "총", example = "2")
        private Integer totalDone;
        @ApiModelProperty(value = "수행률", example = "50")
        private Integer rate;

        @Builder
        public ResponseRoutineDaysRate(LocalDate date) {
            this.date = date;
            this.fullyDone = 0;
            this.partiallyDone = 0;
            this.totalDone = 0;
        }
        public void updateFullyDone() {
            this.fullyDone += 1;
        }
        public void updatePartiallyDone() {
            this.partiallyDone += 1;
        }
        public void updateTotalDone() {
            this.totalDone += 1;
        }
        public void updateRate(int rate) {
            this.rate = rate;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestRoutineSequence {
        @ApiModelProperty(value = "루틴ID 순서", example = "[3, 1, 5, 2]", required = true)
        private ArrayList<Long> sequence = new ArrayList<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestRoutineDto {
        @ApiModelProperty(value = "타이틀", example = "명상", required = true)
        private String title;

        @ApiModelProperty(value = "목표", example = "고요히 자기 자신을 느껴보는 시간입니다.", required = true)
        private String goal;

        @ApiModelProperty(value = "하는 요일", example = "['MON', 'SUN']", required = true)
        private List<Week> days;

        @ApiModelProperty(value = "하는 시간", example = "07:35", required = true)
        private String startTime;

        @ApiModelProperty(value = "카테고리", example = "0", required = true)
        private Integer category;
    }

    @Getter
    @Setter
    public static class ResponseRoutineDto {
        @ApiModelProperty(value = "루틴ID", example = "1")
        private Long id;

        @ApiModelProperty(value = "타이틀", example = "명상")
        private String title;

        @ApiModelProperty(value = "수행여부", example = "DONE")
        private Result result;

        @ApiModelProperty(value = "목표", example = "고요히 자기 자신을 느껴보는 시간입니다.")
        private String goal;

        @ApiModelProperty(value = "하는 요일", example = "['MON', 'SUN']")
        private List<Week> days;

        @ApiModelProperty(value = "하는 시간", example = "07:35")
        private String startTime;

        @ApiModelProperty(value = "카테고리", example = "0")
        private Integer category;

        @Builder
        public ResponseRoutineDto(Routine routine, List<Retrospect> retrospectList) {
            this.id = routine.getId();
            this.title = routine.getTitle();
            this.goal = routine.getGoal();
            this.startTime = routine.getStartTime().toString();
            this.days = routine.getDays().stream().map(RoutineDay::getDay).collect(Collectors.toList());
            this.category = routine.getCategory().getIndex();
            Retrospect retrospect;
            if(retrospectList == null) {
                retrospect = null;
            } else {
             retrospect = retrospectList.stream()
                     .filter(x -> Objects.equals(x.getRoutine().getId(), this.id)).findFirst().orElse(null);
            }
            this.result = retrospect != null ? retrospect.getResult() : Result.NOT;
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

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ResponseRoutineDateListMessageDto {
        private Message message;
        private List<ResponseRoutineDto> data;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ResponseDaysRoutineRateMessageDto {
        private Message message;
        private List<ResponseRoutineDaysRate> data;
        public static ResponseDaysRoutineRateMessageDto of(List<ResponseRoutineDaysRate> daysRateList) {
            daysRateList.forEach(x -> {
                double rate = (x.getFullyDone() + (x.getPartiallyDone() * 0.5)) / x.getTotalDone();
                x.updateRate((int) (rate * 100));
            });
            return ResponseDaysRoutineRateMessageDto.builder().message(
                    Message.builder().status(StatusEnum.DAY_ROUTINE_RATE_OK).msg(DAY_ROUTINE_RATE_OK).build()
            ).data(daysRateList).build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ResponseRecommendedRoutineMessageDto {
        private Message message;
        private List<ResponseRecommendedRoutine> data;
        public static ResponseRecommendedRoutineMessageDto of(List<Organization> recommendedRoutineList) {
            List<ResponseRecommendedRoutine> data = recommendedRoutineList.stream().map(recommended -> {
                int index = recommended.getDescription().indexOf("\\n");
                String description = recommended.getDescription();
                if(index != -1) {
                    description = recommended.getDescription().substring(0, index);
                }
                return ResponseRecommendedRoutine.builder()
                        .title(recommended.getTitle())
                        .category(recommended.getCategory().getIndex())
                        .description(description).build();
            }).collect(Collectors.toList());
            return ResponseRecommendedRoutineMessageDto.builder().message(
                    Message.builder().status(StatusEnum.RECOMMENDED_ROUTINE_OK).msg(RECOMMENDED_ROUTINE_OK).build()
            ).data(data).build();
        }
    }
}