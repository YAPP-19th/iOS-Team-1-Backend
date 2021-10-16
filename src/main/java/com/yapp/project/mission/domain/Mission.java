package com.yapp.project.mission.domain;

import com.yapp.project.account.domain.Account;
import com.yapp.project.organization.domain.Organization;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Mission {

    @Builder
    public Mission(
            Organization organization, Account account, LocalDateTime startDate, LocalDateTime finishDate,
            Integer successCount, Integer failureCount
    ){
        this.organization = organization;
        this.account = account;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.successCount = successCount;
        this.failureCount = failureCount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Organization.class,fetch = FetchType.LAZY)
    private Organization organization;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    private Account account;

    @OneToMany(mappedBy = "mission")
    private final List<Cron> weeks = new ArrayList<>();

    private LocalDateTime startDate;

    private LocalDateTime finishDate;

    private Integer successCount;

    private Integer failureCount;

}
