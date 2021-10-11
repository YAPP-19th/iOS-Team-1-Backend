package com.yapp.project.organization.domain;

import com.yapp.project.organization.domain.dto.OrganizationDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OrganizationTest {
    private Organization organization = Organization.builder().id(1L).title("명상").category("미라클모닝").build();


    @Test
    void test_toResponseDto(){
        OrganizationDto.OrgResponse orgResponse = organization.toResponseDto();
        assertThat(orgResponse.getId()).isEqualTo(organization.getId());
        assertThat(orgResponse.getTitle()).isEqualTo(organization.getTitle());
    }

    @Test
    void test_toDetailResponseDto(){
        OrganizationDto.OrgDetailResponse response = organization.toDetailResponseDto();
        assertThat(response.getId()).isEqualTo(organization.getId());
        assertThat(response.getCategory()).isEqualTo(organization.getCategory());
    }
}