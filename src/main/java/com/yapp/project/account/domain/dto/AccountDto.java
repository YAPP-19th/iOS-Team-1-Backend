package com.yapp.project.account.domain.dto;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class AccountDto {
    private AccountDto(){
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Request{
        private String email;
        private String nickname;
        private String password;
        public Account toAccount(PasswordEncoder passwordEncoder){
            return Account.builder()
                    .email(email)
                    .nickname(nickname)
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

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Response{
        private String email;
        private String nickname;
        private String profile;

        public static AccountDto.Response of(Account account) {
            return new AccountDto.Response(account.getEmail(), account.getNickname(), account.getProfile());
        }
    }

    @Getter
    @AllArgsConstructor
    public static class NicknameRequest{
        private String nickname;
    }
}
