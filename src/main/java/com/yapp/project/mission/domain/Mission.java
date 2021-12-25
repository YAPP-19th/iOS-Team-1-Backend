package com.yapp.project.mission.domain;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.common.Utils;
import com.yapp.project.mission.domain.dto.MissionDto;
import com.yapp.project.organization.domain.Organization;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor
@ToString
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Organization.class,fetch = FetchType.LAZY)
    private Organization organization;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Account account;

    @OneToMany(mappedBy = "mission", cascade = {CascadeType.ALL,CascadeType.REMOVE}, orphanRemoval = true)
    @ToString.Exclude
    private final List<Cron> weeks = new ArrayList<>();

    private LocalDate startDate;

    private LocalDate finishDate;

    private Integer successCount;

    private Integer failureCount;

    private Boolean isAlarm;

    private LocalTime startTime;

    private Boolean isFinish;

    private Boolean isDelete;

    @Builder
    public Mission(
            Organization organization, Account account, LocalDate startDate, LocalDate finishDate,
            Boolean isFinish, Boolean isAlarm, LocalTime startTime
    ){
        this.organization = organization;
        this.account = account;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.isFinish = isFinish;
        this.isAlarm = isAlarm;
        this.startTime = startTime;
    }

    @PrePersist
    public void prePersist(){
        this.isFinish = (this.isFinish!=null)&&this.isFinish;
        this.isDelete = (this.isDelete!=null)&&this.isDelete;
        this.isAlarm = (this.isAlarm!=null)&&this.isAlarm;
        this.successCount = this.successCount!=null?this.successCount:0;
        this.failureCount = this.failureCount!=null?this.failureCount:0;
    }

    public void addDays(List<Cron> days){
        this.weeks.addAll(days);
    }

    public Integer getAchievementRate(){
        if (this.successCount == null){
            this.successCount = 0;
            return 0;
        } else if (this.failureCount == null){
            this.failureCount = 0;
            return 0;
        } else if (this.successCount + this.failureCount == 0){
            return 0;
        }
        return (this.successCount/(this.failureCount+this.successCount))*100;
    }

    public void clickAlarmToggle(){
        this.isAlarm=!this.isAlarm;
    }

    public Integer getPeriod(){
        LocalDate today = DateUtil.KST_LOCAL_DATE_NOW();
        int period = Period.between(today,this.finishDate).getDays();
        return Math.max(period, 0);
    }

    public void updateSuccessCount(){
        this.successCount+=1;
    }

    public void updateFailureCount(){
        this.failureCount+=1;
    }

    public void setStartTimeForTest(LocalTime time) {
        this.startTime = time;
    }

    public void remove(){
        this.isDelete=true;
    }

    public void finishMission(){
        this.isFinish=true;
    }

    public MissionDto.MissionResponse toMissionResponse(){
        return MissionDto.MissionResponse.builder().image(organization.getImage()).title(organization.getTitle())
                .achievementRate(this.getAchievementRate()).period(this.getPeriod()).category(organization.getCategory().getIndex())
                .weeks(weeks.stream().map(Cron::getWeek).collect(Collectors.toList())).build();
    }

    public MissionDto.MissionDetailResponse toMissionDetailResponse(){
        return MissionDto.MissionDetailResponse.builder()
                .category(this.organization.getCategory().getIndex())
                .title(this.organization.getTitle())
                .groupAchievementRate(this.organization.getRate())
                .myAchievementRate(this.getAchievementRate())
                .participant(this.organization.getParticipants())
                .weeks(weeks.stream().map(Cron::getWeek).collect(Collectors.toList()))
                .period(this.getPeriod())
                .endDate(finishDate)
                .beginTime(this.organization.getBeginTime())
                .endTime(this.organization.getEndTime())
                .shoot(this.organization.getShoot())
                .nowPeople(this.organization.getCount())
                .build();
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }

    public void defaultSettingForTest(){
        this.successCount = 0;
        this.failureCount = 0;
        this.isFinish = false;
        this.isDelete = false;
        this.isAlarm = false;
    }

    public void setWeeksForTest(List<Cron> weeks){
        this.weeks.addAll(weeks);
    }

    public void setCountForTest(){
        this.successCount = Utils.randomNumber();
        this.failureCount = Utils.randomNumber();
    }

}
