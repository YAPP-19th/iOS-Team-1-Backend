package com.yapp.project.organization.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class OrganizationDto {
    private OrganizationDto(){
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrgDetailsRequest {
        private Long id;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrgResponse {
        private Long id;
        private String title;
        private Double rate;
        private String image;
        private Integer participant;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class OrgDetailResponse {
        private Long id;
        private String title;
        private Double rate;
        private String recommend;
        private String shoot;
        private String promise;
        private String category;
        private String summary;
        private Integer participant;
    }

}
