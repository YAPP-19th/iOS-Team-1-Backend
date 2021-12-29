package com.yapp.project.mission.domain.dto;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.organization.domain.Category;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.routine.domain.Week;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
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
        @ApiModelProperty(value = "알람 여부", example = "false")
        private Boolean isAlarm;
        @ApiModelProperty(value = "알람 시작 시간", example = "05:00")
        private String startTime;

        public Mission toMission(Account account, Organization organization){
            LocalTime sTime = DateUtil.convertStr2LocalTime(startTime);
            LocalDate sDate = DateUtil.convertStr2LocalDate(startDate);
            LocalDate fDate = DateUtil.convertStr2LocalDate(finishDate);
            return Mission.builder().account(account).startDate(sDate).finishDate(fDate)
                    .organization(organization).isAlarm(isAlarm).startTime(sTime).build();
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MissionDetailResponse{
        @ApiModelProperty(value = "그룹_카테고리",example = "0")
        private Integer category;
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
        @ApiModelProperty(value = "시작시간",example = "5")
        private LocalTime beginTime;
        @ApiModelProperty(value = "끝나는시간",example = "8")
        private LocalTime endTime;
        @ApiModelProperty(value = "현재_미션_클리어한_수",example = "7")
        private Integer nowPeople;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
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
        @ApiModelProperty(value = "카테고리",example = "0")
        private Integer category;
        @ApiModelProperty(value = "오늘 인증 여부",example = "true/false")
        private boolean isTodayCertificate;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
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
