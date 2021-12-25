package com.yapp.project.snapshot.domain;

import com.yapp.project.retrospect.domain.Retrospect;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATETIME_NOW;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Snapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "image")
    @JoinColumn(name = "retrospect_id")
    private Retrospect retrospect;

    private String url;

    private LocalDateTime createdAt;

    @Builder
    public Snapshot(Long id, String url){
        this.id = id;
        this.url = url;
        this.createdAt = KST_LOCAL_DATETIME_NOW();
    }

    public void addRetrospect(Retrospect retrospect){
        this.retrospect = retrospect;
    }
}
