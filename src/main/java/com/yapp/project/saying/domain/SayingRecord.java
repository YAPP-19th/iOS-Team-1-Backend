package com.yapp.project.saying.domain;

import com.yapp.project.account.domain.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class SayingRecord {

    @Builder
    public SayingRecord(Account account, Saying saying){
        this.account = account;
        this.saying = saying;
        this.createdAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    private Account account;

    @ManyToOne(targetEntity = Saying.class, fetch = FetchType.LAZY)
    private Saying saying;

    private LocalDateTime createdAt;
}
