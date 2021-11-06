package com.yapp.project.account.domain.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yapp.project.account.domain.oauth.SocialProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class KakaoResponse implements SocialProperty {
    private String id;
}
