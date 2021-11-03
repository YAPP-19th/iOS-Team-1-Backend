package com.yapp.project.mission.domain.dto;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.utils.DateUtils;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.routine.domain.Week;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

public class MissionDto {
    private MissionDto(){
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MissionRequest{
        @ApiModelProperty(value = "그룹_아이디",example = "1")
        private Long id;
        @ApiModelProperty(value = "미션_시작일",example = "2021-11-01")
        private String startDate;
        @ApiModelProperty(value = "미션_끝나는_날",example = "2021-11-08")
        private String finishDate;
        @ApiModelProperty(value = "미션_하는_요일",example = "[WED, THU, FRI]")
        private List<Week> weeks;

        public Mission toMission(Account account, Organization organization){
            LocalDate sDate = DateUtils.convertStr2LocalDate(startDate);
            LocalDate fDate = DateUtils.convertStr2LocalDate(finishDate);
            return Mission.builder().account(account).startDate(sDate).finishDate(fDate)
                    .organization(organization).build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MissionDetailResponse{
        @ApiModelProperty(value = "그룹_카테고리",example = "미라클모닝")
        private String category;
        @ApiModelProperty(value = "그룹_타이틀",example = "명상")
        private String title;
        @ApiModelProperty(value = "그룹_성취율",example = "80")
        private Integer groupAchievementRate;
        @ApiModelProperty(value = "나의_성취율",example = "80")
        private Integer myAchievementRate;
        @ApiModelProperty(value = "현재_참여자",example = "24")
        private Integer participant;
        @ApiModelProperty(value = "미션_하는_요일",example = "[WED, THU, FRI]")
        private List<Week> weeks;
        @ApiModelProperty(value = "미션_남은_기간",example = "5")
        private Integer period;
        @ApiModelProperty(value = "미션_끝나는_날",example = "2021-11-08")
        private LocalDate endDate;
        @ApiModelProperty(value = "찍어주세요",example = "명상을 할 조용한 장소")
        private String shoot;
        @ApiModelProperty(value = "현재_미션_클리어한_수",example = "7")
        private Integer nowPeople;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MissionDetailResponseMessage{
        private Message message;
        private MissionDetailResponse data;

        public static MissionDetailResponseMessage of(StatusEnum status, String message, MissionDetailResponse data){
            return MissionDetailResponseMessage.builder().data(data).message(
                    Message.builder().msg(message).status(status).build()
            ).build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MissionResponse{
        @ApiModelProperty(value = "그룹_사진",example = "s3/organization/3")
        private String image;
        @ApiModelProperty(value = "미션_남은_기간",example = "5")
        private Integer period;
        @ApiModelProperty(value = "미션_하는_요일",example = "[WED, THU, FRI]")
        private List<Week> weeks;
        @ApiModelProperty(value = "그룹_타이틀",example = "명상")
        private String title;
        @ApiModelProperty(value = "나의_성취율",example = "80")
        private Integer achievementRate;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MissionResponseMessage{
        private Message message;
        private List<MissionResponse> data;

        public static MissionResponseMessage of(StatusEnum status, String message, List<MissionResponse> data){
            return MissionResponseMessage.builder().data(data).message(
                    Message.builder().status(status).msg(message).build()
            ).build();
        }
    }
}
