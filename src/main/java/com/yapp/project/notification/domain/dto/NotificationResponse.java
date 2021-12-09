package com.yapp.project.notification.domain.dto;

import com.yapp.project.aux.Message;
import com.yapp.project.notification.domain.Notification;
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
        private final Long id;
        private String title;
        private String content;
        private Boolean isRead;
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
