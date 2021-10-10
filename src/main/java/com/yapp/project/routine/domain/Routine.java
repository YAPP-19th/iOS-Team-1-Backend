package com.yapp.project.routine.domain;

import com.yapp.project.account.domain.Account;
import com.yapp.project.retrospect.domain.Retrospect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private String title;

    private String goal;

    private LocalTime startTime;

    private LocalDateTime createdAt;

    private Boolean isDelete;

    private String category;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL)
    private List<Cron> crons = new ArrayList<>();

    @OneToMany(mappedBy = "routine", cascade = CascadeType.DETACH)
    private List<Retrospect> retrospects = new ArrayList<>();


    @Builder
    public Routine(Account account, String title, String goal, LocalTime startTime,
                   LocalDateTime createdAt){
        this.account = account;
        this.title = title;
        this.goal = goal;
        this.startTime = startTime;
        this.createdAt = createdAt;
    }
}
