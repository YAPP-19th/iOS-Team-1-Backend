package com.yapp.project.organization.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class OrganizationDto {
    private OrganizationDto(){
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class DetailsRequest{
        private Long id;
    }

}
