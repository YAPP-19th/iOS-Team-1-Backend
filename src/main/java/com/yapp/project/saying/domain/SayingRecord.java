package com.yapp.project.saying.domain;

import com.yapp.project.account.domain.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATETIME_NOW;

@Getter
@Entity
@NoArgsConstructor
public class SayingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Account.class, fetch = FetchType.LAZY)
    private Account account;

    @ManyToOne(targetEntity = Saying.class, fetch = FetchType.LAZY)
    private Saying saying;

    private LocalDateTime createdAt;


    @Builder
    public SayingRecord(Account account, Saying saying){
        this.account = account;
        this.saying = saying;
        this.createdAt = KST_LOCAL_DATETIME_NOW();
    }

}
