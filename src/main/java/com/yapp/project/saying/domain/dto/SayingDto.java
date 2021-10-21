package com.yapp.project.saying.domain.dto;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.saying.domain.Saying;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class SayingDto {
    private SayingDto(){}

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class SayingAccess {
        @ApiModelProperty(value = "명언아이디",example = "15")
        private Long id;
        @ApiModelProperty(value = "내용",example = "새로운 일을 시작하는 용기속에 당신의 천재성, 능력과 기적이 모두 숨어 있다")
        private String content;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SayingRecordResponse{
        @ApiModelProperty(value = "명언 썼는지 확인",example = "True/False")
        private Boolean result;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SayingResponse{
        @ApiModelProperty(value = "명언아이디",example = "15")
        private Long id;
        @ApiModelProperty(value = "명언 썼는지 확인",example = "True/False")
        private Boolean result;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SayingAccessMessage{
        private Message message;
        private Saying data;

        public static SayingAccessMessage of(StatusEnum status, String message, Saying data){
            return SayingAccessMessage.builder()
                    .data(data)
                    .message(Message.builder().status(status).msg(message).build()).build() ;
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SayingResponseMessage{
        private Message message;
        private SayingResponse data;

        public static SayingResponseMessage of(StatusEnum status, String message, SayingResponse data){
            return SayingResponseMessage.builder()
                    .message(Message.builder().status(status).msg(message).build())
                    .data(data)
                    .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SayingRecordResponseMessage{
        private Message message;
        private SayingRecordResponse data;

        public static SayingRecordResponseMessage of(StatusEnum status, String message, SayingRecordResponse data){
            return SayingRecordResponseMessage.builder()
                    .message(Message.builder().status(status).msg(message).build())
                    .data(data)
                    .build();
        }
    }
}
