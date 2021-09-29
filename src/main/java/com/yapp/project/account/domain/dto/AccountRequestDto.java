package com.yapp.project.account.domain.dto;


import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {
    private String email;
    private String username;
    private String password;

    public Account toAccount(PasswordEncoder passwordEncoder){
        return Account.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .authority(Authority.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email,password);
    }
}
