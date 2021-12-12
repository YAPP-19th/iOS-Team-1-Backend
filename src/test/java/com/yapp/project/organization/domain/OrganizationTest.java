package com.yapp.project.organization.domain;

import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.organization.domain.dto.OrgDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OrganizationTest {
    private Organization organization = OrganizationTemplate.makeTestOrganization();


    @Test
    void test_toResponseDto(){
        OrgDto.OrgResponse orgResponse = organization.toResponseDto();
        assertThat(orgResponse.getId()).isEqualTo(organization.getId());
        assertThat(orgResponse.getTitle()).isEqualTo(organization.getTitle());
    }

    @Test
    void test_toDetailResponseDto(){
        OrgDto.OrgDetailResponse response = organization.toDetailResponseDto();
        assertThat(response.getId()).isEqualTo(organization.getId());
        assertThat(response.getCategory()).isEqualTo(organization.getCategory().getIndex());
    }
}