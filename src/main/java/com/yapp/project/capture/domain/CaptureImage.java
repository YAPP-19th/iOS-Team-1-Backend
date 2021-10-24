package com.yapp.project.capture.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@ToString
public class CaptureImage {

    @Builder
    public CaptureImage(Long id, String url, Capture capture){
        this.id=id;
        this.url=url;
        this.capture = capture;
        this.createdAt=LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Capture capture;
}
