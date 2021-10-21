package com.yapp.project.retrospect.domain;

import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.Week;
import com.yapp.project.snapshot.domain.Snapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
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

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Result result;

    private Boolean isReport;

    private LocalDateTime createdAt;

    @Builder
    public Retrospect(Routine routine, Snapshot image, String content, Result result, Boolean isReport){
        this.routine = routine;
        this.image = image;
        this.content = content;
        this.result = result;
        this.isReport = isReport;
        this.createdAt = LocalDateTime.now();
        this.date = LocalDate.now();
    }

    public void updateRetrospect(String content, Snapshot image) {
        this.content = content;
        this.image = image;
        this.image.addRetrospect(this);
    }

    public void updateRetrospect(String content) {
        this.content = content;
    }

    public void deleteImage() {
        this.image = null;
    }


}
