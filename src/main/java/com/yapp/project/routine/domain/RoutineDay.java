package com.yapp.project.routine.domain;

import com.yapp.project.routine.domain.Routine;
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
public class RoutineDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Week day;

    private Long sequence;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;

    private LocalDateTime createdAt;

    @Builder
    public RoutineDay(Week day, Long sequence , Routine routine) {
        this.day = day;
        this.sequence = sequence;
        this.routine = routine;
        this.createdAt = LocalDateTime.now();
    }

    public void updateSequence(Long sequence) {
        this.sequence = sequence;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }
}