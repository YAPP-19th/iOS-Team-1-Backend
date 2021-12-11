package com.yapp.project.notice.domain.dto;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.notice.domain.Notice;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

public class NoticeDto {
    private NoticeDto() {

    }


    @Builder
    @Getter
    public static class NoticeListResponseMessage {
        private Message message;
        private List<Notice> data;

        public static NoticeListResponseMessage of(StatusEnum status, String message,  List<Notice> data) {
            return NoticeListResponseMessage.builder().message(
                    Message.builder().status(status).msg(message).build()
            ).data(data).build();
        }
    }


    @Builder
    @Getter
    @ToString
    public static class NoticeDetailResponseMessage {
        private Message message;
        private Notice data;

        public static NoticeDetailResponseMessage of(StatusEnum status, String message,  Notice data) {
            return NoticeDetailResponseMessage.builder().message(
                    Message.builder().status(status).msg(message).build()
            ).data(data).build();
        }
    }


}
