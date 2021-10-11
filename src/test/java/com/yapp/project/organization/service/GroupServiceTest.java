package com.yapp.project.organization.service;

import static com.yapp.project.aux.test.organization.OrganizationTemplate.*;
import static org.mockito.BDDMockito.given;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.dto.OrganizationDto.*;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private GroupService groupService;

    @Test
    void test_그룹_전체조회(){
        Organization organization = makeTestOrganization();
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);

        given(organizationRepository.findAll()).willReturn(organizations);

        List<OrgResponse> res = groupService.findAll();
        assertThat(res.get(0).getTitle()).isEqualTo(organization.getTitle());
    }

    @Test
    void test_디테일_페이지(){
        Organization organization = makeTestOrganization();
        given(organizationRepository.findById(1L)).willReturn(Optional.ofNullable(organization));
        OrgDetailResponse response = groupService.detailGroup(1L);
        assert organization != null;
        assertThat(response.getTitle()).isEqualTo(organization.getTitle());
        assertThat(response.getSummary()).isEqualTo(organization.getSummary());
    }

    @Test
    void test_카테고리로_그룹_조회_했을_때(){
        Organization organization = makeTestOrganization();
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        given(organizationRepository.findByCategoryAndMore(CATEGORY)).willReturn(organizations);
        List<OrgResponse> res = groupService.findByCategory(CATEGORY);
        OrgResponse orgResponse = res.get(0);
        assertThat(organization.getTitle()).isEqualTo(orgResponse.getTitle());
        assertThat(organization.getId()).isEqualTo(orgResponse.getId());
    }
}