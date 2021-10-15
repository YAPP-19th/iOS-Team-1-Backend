package com.yapp.project.organization.service;

import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.dto.OrgDto;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final OrganizationRepository organizationRepository;

    @Transactional
    public List<OrgDto.OrgResponse> findAll(){
        List<Organization> organizations = organizationRepository.findAll();
        List<OrgDto.OrgResponse> res = new ArrayList<>();
        for(Organization org: organizations){
            res.add(org.toResponseDto());
        }
        return res;
    }

    @Transactional
    public OrgDto.OrgDetailResponse detailGroup(Long id){
        Organization organization = organizationRepository.findById(id).orElse(null);
        assert organization!=null;
        return organization.toDetailResponseDto();
    }

    @Transactional
    public List<OrgDto.OrgResponse> findByCategory(String category){
        List<Organization> organizations = organizationRepository.findByCategoryAndMore(category);
        List<OrgDto.OrgResponse> res = new ArrayList<>();
        for (Organization org : organizations){
            res.add(org.toResponseDto());
        }
        return res;
    }
}
