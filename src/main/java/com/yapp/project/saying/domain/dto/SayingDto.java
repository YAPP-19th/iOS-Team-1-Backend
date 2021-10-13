package com.yapp.project.saying.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class SayingDto {
    private SayingDto(){}

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class SayingAccess {
        private Long id;
        private String content;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SayingResponse{
        private Long id;
        private Boolean result;
    }
}
