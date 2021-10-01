package com.yapp.project.routine.domain;

import com.yapp.project.account.domain.Account;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Routine {

    @Builder
    public Routine(Account account, String title, String purpose, String color, LocalDateTime createdAt, Boolean notification){
        this.account = account;
        this.title = title;
        this.purpose = purpose;
        this.color = color;
        this.createdAt = createdAt;
        this.notification = notification;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Account.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private String title;

    private String purpose;

    private String color;

    private LocalDateTime createdAt;

    private Boolean notification;
}
