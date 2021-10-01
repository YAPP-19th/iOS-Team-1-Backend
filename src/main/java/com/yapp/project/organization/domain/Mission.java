package com.yapp.project.organization.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Mission {

    @Builder
    public Mission(Organization organization, LocalDateTime startDate, LocalDateTime finishDate, Integer successCount, Integer failCount){
        this.organization = organization;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.successCount = successCount;
        this.failCount = failCount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Organization.class,fetch = FetchType.LAZY)
    private Organization organization;

    private LocalDateTime startDate;

    private LocalDateTime finishDate;

    private Integer successCount;

    private Integer failCount;

}
