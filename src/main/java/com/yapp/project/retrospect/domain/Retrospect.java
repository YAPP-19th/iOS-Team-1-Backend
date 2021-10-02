package com.yapp.project.retrospect.domain;

import com.yapp.project.routine.domain.Routine;
import com.yapp.project.snapshot.domain.Snapshot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Retrospect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;

    private String content;

    private LocalDateTime date;

    private String result;

    @OneToOne(mappedBy = "retrospect")
    private Snapshot snapshot;

    private LocalDateTime createdAt;

}
