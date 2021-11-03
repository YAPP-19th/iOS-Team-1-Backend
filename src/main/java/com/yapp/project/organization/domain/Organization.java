package com.yapp.project.organization.domain;

import com.yapp.project.capture.domain.Capture;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.organization.domain.dto.OrgDto;
import lombok.*;

import javax.persistence.*;
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
    }
    @PrePersist
    public void prePersist(){
        this.count = this.count == null ? 0 : this.count;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "organization")
    @ToString.Exclude
    private final List<Mission> missions = new ArrayList<>();

    @OneToMany(mappedBy = "organization")
    @ToString.Exclude
    private final List<Capture> captures = new ArrayList<>();

    private String title;

    private Integer rate;

    private String image;

    private String category;

    private String shoot;

    private Integer count;

    private Integer beginTime;

    private Integer endTime;

    public OrgDto.OrgResponse toResponseDto(){
        return OrgDto.OrgResponse.builder().id(id)
                .title(title).rate(rate)
                .image(image).participant(missions.size()).build();
    }

    public OrgDto.OrgDetailResponse toDetailResponseDto(){
        return OrgDto.OrgDetailResponse.builder().id(id)
                .shoot(shoot).participant(missions.size()).rate(rate).title(title)
                .category(category).startTime(beginTime).finishTime(endTime)
                .build();
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
}
