package com.yapp.project.account.domain.dto;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.Authority;
import com.yapp.project.account.domain.SocialType;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATETIME_NOW;

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
        @ApiModelProperty(value = "소셜토큰", example = "토큰 값")
        private String token;

        public void insertId(String id){
            this.id = id;
        }

        public SocialType getSocial(){
            if (socialType.equalsIgnoreCase(SocialType.KAKAO.name())){
                return SocialType.KAKAO;
            }else {
                return SocialType.APPLE;
            }
        }

        public String getEmail(){
            if (getSocial().equals(SocialType.KAKAO)){
                return id+"@kakao.com";
            }else{
                return id+"@apple.com";
            }
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SocialResponse {
        @ApiModelProperty(value = "진행여부",example = "LOGIN/SIGNUP")
        private String processes;
        @ApiModelProperty(value = "토큰/유저정보")
        private Object data;

        public SocialResponseMessage toSocialResponseMessage(String message){
            return SocialResponseMessage.builder().message(
                    Message.builder()
                            .status(StatusEnum.SOCIAL_OK)
                            .msg(message)
                            .build()
            ).data(this).build();
        }
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
                    .createdAt(KST_LOCAL_DATETIME_NOW())
                    .lastLogin(KST_LOCAL_DATETIME_NOW())
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

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MiddleAccount{
        private String email;
        private SocialType socialType;
    }
}
