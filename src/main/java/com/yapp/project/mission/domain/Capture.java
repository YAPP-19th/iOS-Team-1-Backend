package com.yapp.project.mission.domain;

import com.yapp.project.snapshot.domain.Snapshot;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Capture {

    @Builder
    public Capture(Mission mission, Snapshot snapshot, Long rank){
        this.mission = mission;
        this.snapshot = snapshot;
        this.rank = rank;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Mission.class, fetch = FetchType.LAZY)
    private Mission mission;

    @OneToOne
    @JoinColumn(name = "snapshot_id")
    private Snapshot snapshot;

    private Long rank;

}
