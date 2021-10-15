package com.yapp.project.account.domain.dto;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import com.yapp.project.account.domain.SocialType;
import com.yapp.project.aux.Message;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class SocialDto {
    private SocialDto(){
    }
    @Getter
    @AllArgsConstructor
    @Builder
    public static class SocialRequest {
        private String socialType;
        private String id;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SocialResponse {
        private String processes;
        private Object data;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class SocialResponseMessage{
        private Message message;
        private SocialResponse data;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SocialSignUpRequest {
        private String socialType;
        private String email;
        private String nickname;



        public Account toAccount(PasswordEncoder passwordEncoder, String suffix){
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

    @Getter
    @AllArgsConstructor
    @Builder
    public static class TokenMessage {
        private Message message;
        private TokenDto data;
    }
}
