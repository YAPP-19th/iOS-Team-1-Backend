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

    @Builder
    public Routine(Account account, String title, String goal, String color, LocalDateTime createdAt,
                   LocalTime startTime, Boolean notification){
        this.account = account;
        this.title = title;
        this.goal = goal;
        this.color = color;
        this.createdAt = createdAt;
        this.startTime = startTime;
        this.notification = notification;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Account.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL)
    private List<Cron> crons = new ArrayList<>();

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL)
    private List<Retrospect> retrospects = new ArrayList<>();

    private String title;

    private String goal;

    private String color;

    private LocalTime startTime;

    private LocalDateTime createdAt;

    private Boolean notification;
}
