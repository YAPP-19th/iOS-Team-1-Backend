package com.yapp.project.mission.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yapp.project.routine.domain.Week;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Week week;

    @ManyToOne(targetEntity = Mission.class, fetch = FetchType.LAZY)
    @JsonBackReference
    private Mission mission;

    private LocalDateTime createdAt;

    @Builder
    public Cron(Week week, Mission mission) {
        this.week = week;
        this.mission = mission;
        this.createdAt = LocalDateTime.now();
    }

}