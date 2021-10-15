package com.yapp.project.organization.domain.dto;

import com.yapp.project.aux.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

public class OrgDto {
    private OrgDto(){
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
    public static class OrgResponseMessage{
        private Message message;
        private OrgResponse data;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrgListResponseMessage{
        private Message message;
        private List<OrgResponse> data;
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

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrgDetailMessage{
        private Message message;
        private OrgDetailResponse data;
    }
}
