package com.yapp.project.retrospect.domain.dto;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.routine.domain.RoutineDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RetrospectDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class RequestRetrospectResult {
        @ApiModelProperty(value = "루틴ID 보내주세요.", example = "2", required = true)
        private Long routineId;
        @ApiModelProperty(value = "루틴의 수행일 보내주세요.\n만약 11일 루틴인데 12일 작성한다면 11일을 보내주세요", example = "2021-10-13", required = true)
        private String date;
        @ApiModelProperty(value = "수행 결과 보내주세요.", example = "DONE", required = true)
        private Result result;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class RequestUpdateRetrospect {
        @ApiModelProperty(value = "회고ID 보내주세요.", example = "2", required = true)
        private Long retrospectId;
        @ApiModelProperty(value = "회고 내용 보내주세요.", example = "알찬 러닝이었다.", required = true)
        private String content;
        @ApiModelProperty(value = "이미지 파일 보내주세요.", example = "multipart/form-data형식으로 보내주세요", required = true)
        private MultipartFile image;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class RequestRetrospect {
        @ApiModelProperty(value = "루틴ID 보내주세요.", example = "2", required = true)
        private Long routineId;
        @ApiModelProperty(value = "회고 내용 보내주세요.", example = "알찬 러닝이었다.", required = true)
        private String content;
        @ApiModelProperty(value = "루틴의 수행일 보내주세요.\n만약 11일 루틴인데 12일 작성한다면 11일을 보내주세요", example = "2021-10-13", required = true)
        private String date;
        @ApiModelProperty(value = "이미지 파일 보내주세요.", example = "multipart/form-data형식으로 보내주세요", required = true)
        private MultipartFile image;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ResponseRetrospect {
        @ApiModelProperty(value = "회고ID", example = "1")
        private Long id;
        @ApiModelProperty(value = "회고 내용", example = "알찬 러닝이었다.")
        private String content;
        @ApiModelProperty(value = "루틴 수행일", example = "2021-10-21")
        private LocalDate date;
        @ApiModelProperty(value = "이미지 경로", example = "이미지 경로입니다.")
        private String image;
        @ApiModelProperty(value = "수행 여부", example = "DONE")
        private Result result;
        private RoutineDTO.ResponseRoutineDto routine;

        @Builder
        public ResponseRetrospect(Retrospect retrospect, RoutineDTO.ResponseRoutineDto routine){
            this.id = retrospect.getId();
            this.routine = routine;
            this.content = retrospect.getContent();
            this.date = retrospect.getDate();
            this.result = retrospect.getResult();
            if(retrospect.getImage() != null)
                this.image = retrospect.getImage().getUrl();
            else this.image = null;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ResponseRetrospectMessage {
        private Message message;
        private ResponseRetrospect data;

        public static ResponseRetrospectMessage of(StatusEnum status, String msg, Retrospect retrospect){
            return RetrospectDTO.ResponseRetrospectMessage.builder()
                    .message(Message.builder().msg(msg).status(status).build())
                    .data(RetrospectDTO.ResponseRetrospect.builder()
                            .retrospect(retrospect)
                            .routine(RoutineDTO.ResponseRoutineDto.builder()
                                    .routine(retrospect.getRoutine()).build()).build()).build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ResponseRetrospectListMessage {
        private Message message;
        private List<ResponseRetrospect> data;

        public static ResponseRetrospectListMessage of(StatusEnum status, String msg, List<Retrospect> retrospectList) {
            List<RetrospectDTO.ResponseRetrospect> getRetrospectList = retrospectList.stream().map(x ->
                    RetrospectDTO.ResponseRetrospect.builder()
                            .retrospect(x)
                            .routine(RoutineDTO.ResponseRoutineDto.builder().routine(x.getRoutine()).build()).build()).collect(Collectors.toList());
            return RetrospectDTO.ResponseRetrospectListMessage.builder()
                    .message(Message.builder().msg(msg).status(status).build())
                    .data(getRetrospectList).build();
        }
    }
}