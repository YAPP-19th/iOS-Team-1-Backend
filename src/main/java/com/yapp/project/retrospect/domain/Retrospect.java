package com.yapp.project.retrospect.domain;

import com.yapp.project.routine.domain.Routine;
import com.yapp.project.snapshot.domain.Snapshot;
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
public class Retrospect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Snapshot image;

    private String content;

    private LocalDateTime date;

    private String result;

    private Boolean isReport;

    private LocalDateTime createdAt;

    @Builder
    public Retrospect(Routine routine, Snapshot image, String content, String result, Boolean isReport,
                   LocalDateTime createdAt){
        this.routine = routine;
        this.image = image;
        this.content = content;
        this.result = result;
        this.isReport = isReport;
        this.createdAt = createdAt;
    }

}
