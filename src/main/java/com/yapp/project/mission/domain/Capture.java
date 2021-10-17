package com.yapp.project.mission.domain;

import com.yapp.project.organization.domain.Organization;
import com.yapp.project.snapshot.domain.Snapshot;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Capture {

    @Builder
    public Capture(Mission mission, Organization organization,
                   Snapshot snapshot, Long rank, Achievement achievement,LocalDateTime createdAt){
        this.organization = organization;
        this.mission = mission;
        this.snapshot = snapshot;
        this.rank = rank;
        this.myAchievementRate = achievement.getMyAchievementRate();
        this.groupAchievementRate = achievement.getGroupAchievementRate();
        this.createdAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Mission.class, fetch = FetchType.LAZY)
    private Mission mission;

    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    private Organization organization;

    @OneToOne
    @JoinColumn(name = "snapshot_id")
    private Snapshot snapshot;

    private Long rank;

    private Integer myAchievementRate;

    private Integer groupAchievementRate;

    private LocalDateTime createdAt;

}
