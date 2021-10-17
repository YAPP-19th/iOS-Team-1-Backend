package com.yapp.project.organization.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.mission.domain.dao.MissionOrganization;
import com.yapp.project.mission.domain.repository.MissionRepository;
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
    private final MissionRepository missionRepository;

    @Transactional
    public List<OrgDto.OrgResponse> findAll(Account account){
        ArrayList<Long> excludeOrganization = getMyOrganizationId(account);

        List<Organization> organizations;
        if (!excludeOrganization.isEmpty())
            organizations = organizationRepository.findOrganizationsNotIn(excludeOrganization);
        else
            organizations = organizationRepository.findAll();

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
    public List<OrgDto.OrgResponse> findByCategory(String category, Account account){
        ArrayList<Long> excludeOrganization = getMyOrganizationId(account);

        List<Organization> organizations;
        if (!excludeOrganization.isEmpty())
            organizations= organizationRepository.findByCategoryAndMoreAndNotIn(category, excludeOrganization);
        else
            organizations = organizationRepository.findByCategoryAndMore(category);

        List<OrgDto.OrgResponse> res = new ArrayList<>();
        for (Organization org : organizations){
            res.add(org.toResponseDto());
        }
        return res;
    }

    private ArrayList<Long> getMyOrganizationId(Account account){
        ArrayList<MissionOrganization> missions = missionRepository.findMissionByAccountAndIsFinishIsFalse(account);
        ArrayList<Long> excludeOrganization = new ArrayList<>();
        for (MissionOrganization mission : missions){
            excludeOrganization.add(mission.getOrganization().getId());
        }
        return excludeOrganization;
    }
}
