package com.yapp.project.organization.domain;

import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.capture.domain.Capture;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Capture> captures = new ArrayList<>();

    private String title;

    private Integer rate;

    private Integer participants;

    private String image;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String shoot;

    private Integer count;

    private Integer groupSuccessCount;

    private Integer groupFailCount;

    private LocalDate updatedAt;

    private LocalTime beginTime;

    private LocalTime endTime;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(columnDefinition="TEXT")
    private String recommend;

    @Builder
    public Organization(String title , Integer rate, Category category, Clause clause){
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
        this.count = 0;
        this.groupSuccessCount = 0;
        this.groupFailCount = 0;
        this.participants = 0;
    }

    public void updateParticipants(Integer participants) {
        this.participants = participants;
    }

    public void updateCurrentCount(){
        this.count = count==null? 0: count;
        this.count+=1;
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

    public OrgDto.OrgResponse toResponseDto(){
        return OrgDto.OrgResponse.builder().id(id)
                .title(title).rate(rate).category(category.getIndex())
                .image(image).participant(participants).build();
    }

    public OrgDto.OrgDetailResponse toDetailResponseDto(){
        return OrgDto.OrgDetailResponse.builder().id(id)
                .shoot(shoot).participant(participants).rate(rate).title(title)
                .category(category.getIndex()).beginTime(beginTime).endTime(endTime)
                .description(description).recommend(recommend).build();
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }

    public void defaultSettingForTest(){
        this.count = 0;
        this.rate = 0;
    }

}
