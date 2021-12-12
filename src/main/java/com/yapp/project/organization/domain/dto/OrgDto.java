package com.yapp.project.organization.domain.dto;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.organization.domain.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;
import java.util.List;

public class OrgDto {
    private OrgDto(){
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrgDetailsRequest {
        @ApiModelProperty(value = "그룹아이디",example = "15")
        private Long id;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrgResponse {
        @ApiModelProperty(value = "그룹아이디",example = "15")
        private Long id;
        @ApiModelProperty(value = "그룹제목",example = "명상")
        private String title;
        @ApiModelProperty(value = "달성률",example = "87")
        private Integer rate;
        @ApiModelProperty(value = "이미지",example = "s3/organization/15")
        private String image;
        @ApiModelProperty(value = "참여자",example = "25")
        private Integer participant;
        @ApiModelProperty(value = "카테고리",example = "0")
        private  Integer category;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrgResponseMessage{
        private Message message;
        private OrgResponse data;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrgListResponseMessage{
        private Message message;
        private List<OrgResponse> data;

        public static OrgListResponseMessage of(StatusEnum status, String message, List<OrgResponse> data){
            return OrgListResponseMessage.builder().data(data).message(
                    Message.builder().msg(message).status(status).build()
            ).build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class OrgDetailResponse {
        @ApiModelProperty(value = "그룹아이디",example = "15")
        private Long id;
        @ApiModelProperty(value = "그룹제목",example = "명상")
        private String title;
        @ApiModelProperty(value = "달성률",example = "87")
        private Integer rate;
        @ApiModelProperty(value = "찍어주세요",example = "명상을 할 조용한 장소")
        private String shoot;
        @ApiModelProperty(value = "카테고리",example = "0")
        private Integer category;
        @ApiModelProperty(value = "참여자",example = "25")
        private Integer participant;
        @ApiModelProperty(value = "시작시간", example = "5")
        private LocalTime beginTime;
        @ApiModelProperty(value = "끝나는 시간", example = "8")
        private LocalTime endTime;
        @ApiModelProperty(value = "설명글", example = "고요히 자기 자신을 느껴보는 시간입니다. ")
        private String description;
        @ApiModelProperty(value = "이렇게 해보세요", example = "명상을 처음 접해본다면, 호흡부터 시작해보세요.  ")
        private String recommend;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrgDetailMessage{
        private Message message;
        private OrgDetailResponse data;

        public static OrgDetailMessage of(StatusEnum status, String message, OrgDetailResponse data){
            return OrgDetailMessage.builder().data(data).message(
                    Message.builder().status(status).msg(message).build()
            ).build();
        }
    }
}
