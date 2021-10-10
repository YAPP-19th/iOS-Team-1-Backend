package com.yapp.project.routine.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
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
}
