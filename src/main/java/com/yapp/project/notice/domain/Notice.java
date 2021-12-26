package com.yapp.project.notice.domain;

import com.yapp.project.aux.common.DateUtil;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition="TEXT")
    private String content;

    private LocalDate createdAt;

    @Builder
    public Notice(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = DateUtil.KST_LOCAL_DATE_NOW();
    }

    public void setContentForTest(String content) {
        this.content = content;
    }

}
