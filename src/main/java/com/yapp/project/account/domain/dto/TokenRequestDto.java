package com.yapp.project.account.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequestDto {
    @ApiModelProperty(value = "갱신토큰값",example = "eye78232fdfdsfs87sd5sd2dfs")
    private String refreshToken;
}
