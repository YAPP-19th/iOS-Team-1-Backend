package com.yapp.project.organization.domain;

import com.yapp.project.mission.domain.Capture;
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
        this.recommend = clause.getRecommend();
        this.shoot = clause.getShoot();
        this.promise = clause.getPromise();
        this.summary = clause.getSummary();
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

    private String recommend;

    private String shoot;

    private String promise;

    private String summary;

    private Integer count;

    public void setCaptures(List<Capture> captures) {
        this.captures = captures;
    }

    public OrgDto.OrgResponse toResponseDto(){
        return OrgDto.OrgResponse.builder().id(id)
                .title(title).rate(rate)
                .image(image).participant(missions.size()).build();
    }

    public OrgDto.OrgDetailResponse toDetailResponseDto(){
        return OrgDto.OrgDetailResponse.builder().id(id)
                .shoot(shoot).participant(missions.size()).rate(rate).title(title)
                .recommend(recommend).promise(promise).category(category)
                .summary(summary).build();
    }


}
