package com.yapp.project.organization.domain;

import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.capture.domain.Capture;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.organization.domain.dto.OrgDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Organization {

    @Builder
    public Organization(Long id, String title , Integer rate, String category, Clause clause){
        this.id = id;
        this.title = title;
        this.rate = rate;
        this.category = category;
        this.shoot = clause.getShoot();
        this.beginTime = clause.getBeginTime();
        this.endTime = clause.getEndTime();
        this.description = clause.getDescription();
        this.recommend = clause.getRecommend();
        this.updatedAt = DateUtil.KST_LOCAL_DATE_NOW();

    }

    @PrePersist
    public void prePersist(){
        this.count = this.count == null ? 0 : this.count;
        this.groupSuccessCount = this.groupSuccessCount == null ? 0 : this.groupSuccessCount;
        this.groupFailCount = this.groupFailCount == null ? 0 : this.groupFailCount;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "organization")
    @ToString.Exclude
    private final List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "organization")
    @ToString.Exclude
    private List<Capture> captures = new ArrayList<>();

    private String title;

    private Integer rate;

    private String image;

    private String category;

    private String shoot;

    private Integer count;

    private Integer groupSuccessCount;

    private Integer groupFailCount;

    private LocalDate updatedAt;

    private LocalTime beginTime;

    private LocalTime endTime;

    @Lob
    private String description;

    @Lob
    private String recommend;

    public OrgDto.OrgResponse toResponseDto(){
        return OrgDto.OrgResponse.builder().id(id)
                .title(title).rate(rate)
                .image(image).participant(missions.size()).build();
    }

    public OrgDto.OrgDetailResponse toDetailResponseDto(){
        return OrgDto.OrgDetailResponse.builder().id(id)
                .shoot(shoot).participant(missions.size()).rate(rate).title(title)
                .category(category).beginTime(beginTime).endTime(endTime).description(description).recommend(recommend).build();
    }

    public void updateCurrentCount(){
        this.count = count==null? 0: count;
        this.count+=1;
    }

    public void defaultSetting(){
        this.count = 0;
        this.rate = 0;
    }

    public Integer getCount(){
        return count!=null?count:0;
    }

    public void beforeBatchInitAboutRate(){
        this.groupSuccessCount = 0;
        this.groupFailCount = 0;
    }

    public void addMissionRateOnGroup(Integer successCount, Integer failCount){
        if (this.groupSuccessCount==null){
            this.groupSuccessCount = 0;
        }
        if (this.groupFailCount==null){
            this.groupFailCount = 0;
        }
        this.groupSuccessCount += successCount;
        this.groupFailCount += failCount;
    }

    public void updateRate(){
        double decimal = (double) this.groupSuccessCount/(this.groupSuccessCount+this.groupFailCount);
        this.rate = (int)(100 * decimal);
    }

    public void updateUpdatedAtAndCountZero(){
        this.updatedAt = DateUtil.KST_LOCAL_DATE_NOW();
        this.count=0;
    }
}
