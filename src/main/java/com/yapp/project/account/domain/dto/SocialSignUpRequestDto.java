package com.yapp.project.account.domain.dto;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import com.yapp.project.account.domain.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SocialSignUpRequestDto {
    private String socialType;
    private String email;
    private String nickname;



    public Account toAccount(PasswordEncoder passwordEncoder,String suffix){
        return Account.builder()
                .socialType(SocialType.valueOf(socialType))
                .authority(Authority.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .nickname(nickname)
                .email(email)
                .password(passwordEncoder.encode(email+suffix))
                .build();
    }

}
