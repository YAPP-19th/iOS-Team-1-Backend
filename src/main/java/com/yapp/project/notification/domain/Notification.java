package com.yapp.project.notification.domain;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.common.DateUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    private String title;

    private String content;

    private Boolean isRead;

    private LocalDate date;

    @Builder
    public Notification(Account account, String title, String content) {
        this.account = account;
        this.title = title;
        this.content = content;
        this.isRead = false;
        this.date = DateUtil.KST_LOCAL_DATE_NOW();
    }

    public void updateRead() {
        this.isRead = true;
    }
}
