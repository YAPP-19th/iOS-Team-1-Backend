package com.yapp.project.account.domain;

import com.yapp.project.account.domain.dto.AccountDto.*;
import com.yapp.project.notification.domain.Notification;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATETIME_NOW;

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

    @Column(nullable = false,unique = false)
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

    private Boolean isAlarm;

    @PrePersist
    public void prePersist() {
        this.isDelete= false;
        this.isAlarm= false;
    }

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private final List<Notification> notificationLists = new ArrayList<>();

    public void setIsAlarm(Boolean isOn){
        this.isAlarm = isOn;
    }

    public void updateLastLoginAccount(){
        this.lastLogin = KST_LOCAL_DATETIME_NOW();
    }

    public void updateFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    public UserRequest toAccountRequestDto(String suffix){
        return new UserRequest(email,nickname,email+suffix,socialType,fcmToken);
    }

    public void resetPassword(PasswordEncoder passwordEncoder, String newPassword){
        this.password = passwordEncoder.encode(newPassword);
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }

    public void changeProfileImage(String profile) {
        this.profile = profile;
    }

}
