package com.yapp.project.account.domain.dto;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import com.yapp.project.account.domain.SocialType;
import com.yapp.project.aux.Message;
import io.swagger.annotations.ApiModelProperty;
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
        @ApiModelProperty(value = "소셜타입",example = "KAKAO/APPLE")
        private String socialType;
        @ApiModelProperty(value = "소셜해당아이디",example = "142342124")
        private String id;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SocialResponse {
        @ApiModelProperty(value = "진행여부",example = "LOGIN/SIGNUP")
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
        @ApiModelProperty(value = "소셜타입",example = "KAKAO/APPLE")
        private String socialType;
        @ApiModelProperty(value = "이메일",example = "minning@example.com")
        private String email;
        @ApiModelProperty(value = "닉네임",example = "미닝")
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
