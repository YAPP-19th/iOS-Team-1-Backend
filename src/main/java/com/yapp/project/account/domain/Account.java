package com.yapp.project.account.domain;

import com.yapp.project.account.domain.dto.AccountDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private  String profile;

    @Column(columnDefinition = "integer default 1")
    private int level;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    private Boolean isDelete;

    private String fcmToken;

    public void updateLastLoginAccount(){
        this.lastLogin = LocalDateTime.now();
    }

    public AccountDto.Request toAccountRequestDto(String suffix){
        return new AccountDto.Request(email,nickname,email+suffix);
    }

}
