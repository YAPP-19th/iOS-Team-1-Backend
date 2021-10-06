package com.yapp.project.organization.service;

import com.yapp.project.aux.Message;
import com.yapp.project.organization.domain.dto.OrganizationDto;

public interface GroupService {
    Message findAll();
    Message findByThemes(OrganizationDto.FindThemeRequest request);
    Message showGroupDetails(OrganizationDto.DetailsRequest request);
    Message joinGroup(OrganizationDto.JoinRequest request);
    Message showMyGroupLists();
}
