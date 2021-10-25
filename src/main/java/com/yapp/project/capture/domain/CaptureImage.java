package com.yapp.project.capture.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATETIME_NOW;

@Entity
@NoArgsConstructor
@ToString
public class CaptureImage {

    @Builder
    public CaptureImage(Long id, String url, Capture capture){
        this.id=id;
        this.url=url;
        this.capture = capture;
        this.createdAt= KST_LOCAL_DATETIME_NOW();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Capture capture;
}
