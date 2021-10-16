package com.yapp.project.account.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TokenDto {
    @ApiModelProperty(value = "인증접두사",example = "BEARER")
    private String grantType;
    @ApiModelProperty(value = "인증토큰값",example = "eye32451dsf34451ds68fads153123543df45asdf")
    private String accessToken;
    @ApiModelProperty(value = "갱신토큰값",example = "eye84sd5f56231dsfd8d4sda235fd78sd1ads")
    private String refreshToken;
    @ApiModelProperty(value = "인증토큰만료기간",example = "112479853")
    private Long accessTokenExpiresIn;
}
