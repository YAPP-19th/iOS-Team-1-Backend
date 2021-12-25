package com.yapp.project.capture.domain;

import com.yapp.project.mission.domain.Mission;
import com.yapp.project.capture.domain.dto.CaptureDto;
import com.yapp.project.organization.domain.Organization;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATETIME_NOW;

@Getter
@Entity
@NoArgsConstructor
public class Capture {

    @Builder
    public Capture(Long id, Mission mission, Organization organization, Integer rank, Achievement achievement){
        this.id = id;
        this.organization = organization;
        this.mission = mission;
        this.rank = rank;
        this.myAchievementRate = achievement.getMyAchievementRate();
        this.groupAchievementRate = achievement.getGroupAchievementRate();
        this.createdAt = KST_LOCAL_DATETIME_NOW();
    }

    @PrePersist
    public void prePersist(){
        this.isDelete= isDelete != null && isDelete;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Mission.class, fetch = FetchType.LAZY)
    private Mission mission;

    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    private Organization organization;

    @OneToMany(mappedBy = "capture", fetch = FetchType.EAGER, cascade = {CascadeType.ALL, CascadeType.REMOVE}, orphanRemoval = true)
    private final List<CaptureImage> captureImage = new ArrayList<>();

    private Integer rank;

    private Integer myAchievementRate;

    private Integer groupAchievementRate;

    private Boolean isDelete;

    private LocalDateTime createdAt;

    public void updateCaptureImage(CaptureImage images){
        captureImage.add(images);
    }

    public void remove(){
        this.isDelete=true;
    }

    public CaptureDto.CaptureResponse toCaptureResponse(){
        return CaptureDto.CaptureResponse.builder().images(captureImage).captureId(id)
                .build();
    }

}
