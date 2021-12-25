package com.yapp.project.routine.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATETIME_NOW;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoutineDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Week day;

    private Long sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    private LocalDateTime createdAt;

    @Builder
    public RoutineDay(Week day, Long sequence , Routine routine) {
        this.day = day;
        this.sequence = sequence;
        this.routine = routine;
        this.createdAt = KST_LOCAL_DATETIME_NOW();
    }

    public void updateSequence(Long sequence) {
        this.sequence = sequence;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }
}