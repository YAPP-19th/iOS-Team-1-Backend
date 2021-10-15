package com.yapp.project.account.domain.dto;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import com.yapp.project.aux.Message;
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
    public static class UserRequest {
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
    public static class UserResponse {
        private String email;
        private String nickname;
        private String profile;

        public static UserResponse of(Account account) {
            return new UserResponse(account.getEmail(), account.getNickname(), account.getProfile());
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserResponseMessage{
        private Message message;
        private UserResponse data;
    }


    @Getter
    @AllArgsConstructor
    public static class NicknameRequest{
        private String nickname;
    }
}
