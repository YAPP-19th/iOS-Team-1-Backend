package com.yapp.project.snapshot.domain;

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

    private String url;

    private LocalDateTime createdAt;

    @Builder
    public Snapshot(String url){
        this.url = url;
        this.createdAt = LocalDateTime.now();
    }
}
