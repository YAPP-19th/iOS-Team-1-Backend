package com.yapp.project.organization.domain.dto;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
        @ApiModelProperty(value = "이렇게해보세",example = "명상을 처음 접해본다면, 호흡부터 시작해보세요.  \n" +
                "기도를 통해 들어가 발끝까지 뻗어나가는 호흡을 조용히 느껴보며 머리의 생각을 비워보세요.  \n" +
                "집중이 힘들다면 조용한 음악이나 자연의 소리를 틀어보는 것도 나쁘지 않아요. \n" +
                "소리에 귀를 기울이며 명상에 몰입해보세요.")
        private String recommend;
        @ApiModelProperty(value = "찍어주세요",example = "명상을 할 조용한 장소")
        private String shoot;
        @ApiModelProperty(value = "지켜주세요",example = "오전 5시 ~ 8시 사이 사진 업로드")
        private String promise;
        @ApiModelProperty(value = "카테고리",example = "미라클모닝")
        private String category;
        @ApiModelProperty(value = "요약",example = "오늘도 나만의 중심을 단단하게 다져보세요")
        private String summary;
        @ApiModelProperty(value = "참여자",example = "25")
        private Integer participant;
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
