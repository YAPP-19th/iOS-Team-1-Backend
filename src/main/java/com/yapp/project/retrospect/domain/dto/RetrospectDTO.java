package com.yapp.project.retrospect.domain.dto;

import com.yapp.project.aux.Message;
import com.yapp.project.retrospect.domain.Retrospect;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class RetrospectDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestRetrospect {
        @ApiModelProperty(value = "루틴ID 보내주세요.", example = "2", required = true)
        private Long routineId;
        @ApiModelProperty(value = "회고 내용 보내주세요.", example = "알찬 러닝이었다.", required = true)
        private String content;
        @ApiModelProperty(value = "이미지 파일 보내주세요.", example = "multipart/form-data형식으로 보내주세요", required = true)
        private MultipartFile image;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ResponseRetrospect {
        @ApiModelProperty(value = "회고ID", example = "1", required = true)
        private Long id;
        @ApiModelProperty(value = "루틴ID", example = "2", required = true)
        private Long routineId;
        @ApiModelProperty(value = "회고 내용", example = "알찬 러닝이었다.", required = true)
        private String content;
        @ApiModelProperty(value = "회고 작성일", example = "2021-10-21", required = true)
        private LocalDate date;
        @ApiModelProperty(value = "이미지 경로", example = "이미지 경로입니다.", required = true)
        private String image;

        @Builder
        public ResponseRetrospect(Retrospect retrospect){
            this.id = retrospect.getId();
            this.routineId = retrospect.getRoutine().getId();
            this.content = retrospect.getContent();
            this.date = retrospect.getDate();
            if(retrospect.getImage() != null)
                this.image = retrospect.getImage().getUrl();
            else this.image = null;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class RequestRetrospectMessage {
        private Message message;
        private ResponseRetrospect data;
    }
}