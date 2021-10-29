package com.yapp.project.retrospect.domain;

import com.yapp.project.routine.domain.Routine;
import com.yapp.project.snapshot.domain.Snapshot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATETIME_NOW;
import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATE_NOW;

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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
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
        this.createdAt = KST_LOCAL_DATETIME_NOW();
        this.date = KST_LOCAL_DATE_NOW();
    }

    public void updateRetrospect(String content, Snapshot image) {
        this.content = content;
        this.image = image;
        this.image.addRetrospect(this);
    }

    public void updateRetrospect(String content) {
        this.content = content;
    }

    public void updateRetrospect(Snapshot image) {
        this.image = image;
        this.image.addRetrospect(this);
    }

    public void deleteImage() {
        this.image = null;
    }

    public void updateResult(Result result) {
        this.result = result;
    }

    public void updateIsReport() {
        this.isReport = true;
    }

}
