package com.yapp.project.notification.domain.dto;

import com.yapp.project.aux.Message;
import com.yapp.project.notification.domain.Notification;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NotificationResponse {
    private List<NotificationDto> data;
    private Message message;

    @Builder
    public NotificationResponse(List<Notification> data, Message message) {
        this.data = data.stream().map( x ->
                NotificationDto.builder().notification(x).build()).collect(Collectors.toList());
        this.message = message;
    }

    @Getter
    public static class NotificationDto {
        @ApiModelProperty(value = "내역 고유번호", example = "1")
        private Long id;
        @ApiModelProperty(value = "제목", example = "새로운 리포트가 도착했어요")
        private String title;
        @ApiModelProperty(value = "내용", example = "2021년 11월의 리포트가 도착했습니다.")
        private String content;
        @ApiModelProperty(value = "읽음 여부", example = "false")
        private Boolean isRead;
        @ApiModelProperty(value = "생성일", example = "2021-12-07")
        private LocalDate date;

        @Builder
        public NotificationDto(Notification notification) {
            this.id = notification.getId();
            this.title = notification.getTitle();
            this.content = notification.getContent();
            this.isRead = notification.getIsRead();
            this.date = notification.getDate();
        }
    }

}
