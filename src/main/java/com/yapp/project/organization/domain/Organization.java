package com.yapp.project.organization.domain;

import com.yapp.project.mission.domain.Mission;
import com.yapp.project.organization.domain.dto.OrganizationDto.*;
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
    public Organization(Long id, String title ,String category,String summary){
        this.id = id;
        this.title = title;
        this.category = category;
        this.summary = summary;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "organization")
    @ToString.Exclude
    private final List<Mission> missions = new ArrayList<>();

    private String title;

    private Double rate;

    private String image;

    private String recommend;

    private String shoot;

    private String promise;

    private String category;

    private String summary;

    private Long count;

    public OrgResponse toResponseDto(){
        return OrgResponse.builder().id(id)
                .title(title).rate(rate)
                .image(image).participant(missions.size()).build();
    }

    public OrgDetailResponse toDetailResponseDto(){
        return OrgDetailResponse.builder().id(id)
                .shoot(shoot).participant(missions.size()).rate(rate).title(title)
                .recommend(recommend).promise(promise).category(category)
                .summary(summary).build();
    }


}
