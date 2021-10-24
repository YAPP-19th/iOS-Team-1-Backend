package com.yapp.project.capture.domain.dto;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.capture.domain.CaptureImage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class CaptureDto {
    private CaptureDto(){
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class CaptureRequest{
        @ApiModelProperty(value = "미션 아이디", example = "32")
        private Long missionId;

        @ApiModelProperty(value = "인증 사진", example = "multipart/form-data 형식으로 보내주세요", required = true)
        private MultipartFile image;
    }


    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class DeleteIdListRequest {
        @ApiModelProperty(value = "캡처 아이디들", example = "32")
        private List<Long> captureIdLists;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class CaptureSuccessResponse {
        @ApiModelProperty(value = "성공 여부", example = "true")
        private Boolean result;
    }


    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class CaptureResponse {
        @ApiModelProperty(value = "캡처 아이디", example = "1")
        private Long captureId;

        @ApiModelProperty(value = "사진 경로", example = "s3/capture/capture_1_20211023")
        private List<CaptureImage> images;
    }


    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class CaptureListResponse {
        @ApiModelProperty(value = "이미지들")
        List<CaptureResponse> captures;
    }


    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class CaptureListResponseMessage {
        private Message message;
        private CaptureListResponse data;

        public static CaptureListResponseMessage of(StatusEnum status, String message, CaptureListResponse data){
            return CaptureListResponseMessage.builder().message(
                    Message.builder().msg(message).status(status).build()
            ).data(data).build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class CaptureResponseMessage{
        private Message message;
        private CaptureSuccessResponse data;

        public static CaptureResponseMessage of(StatusEnum status, String message, CaptureSuccessResponse data){
            return CaptureResponseMessage.builder().message(
                    Message.builder().msg(message).status(status).build()
            ).data(data).build();
        }
    }
}
