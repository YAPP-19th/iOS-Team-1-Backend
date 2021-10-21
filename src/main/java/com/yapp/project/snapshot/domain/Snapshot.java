package com.yapp.project.snapshot.domain;

import com.yapp.project.mission.domain.Capture;
import com.yapp.project.retrospect.domain.Retrospect;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Snapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "snapshot")
    @JoinColumn(name = "capture_id")
    private Capture capture;

    @OneToOne(mappedBy = "image")
    @JoinColumn(name = "retrospect_id")
    private Retrospect retrospect;

    private String url;

    private LocalDateTime createdAt;

    @Builder
    public Snapshot(String url){
        this.url = url;
        this.createdAt = LocalDateTime.now();
    }

    public void addRetrospect(Retrospect retrospect){
        this.retrospect = retrospect;
    }
}
