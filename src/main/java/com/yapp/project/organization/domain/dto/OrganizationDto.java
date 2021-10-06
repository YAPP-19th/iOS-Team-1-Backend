package com.yapp.project.organization.domain.dto;

import com.yapp.project.routine.domain.Week;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class OrganizationDto {
    private OrganizationDto(){
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class FindThemeRequest {
        private String theme;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class DetailsRequest{
        private Long id;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class JoinRequest{
        private Integer term;
        private String theme;
        private Boolean notification;
        private List<Week> days;
        // 미정
    }


}
