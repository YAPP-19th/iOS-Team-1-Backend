package com.yapp.project.organization.controller;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.organization.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import static com.yapp.project.aux.content.OrganizationContent.*;
import static com.yapp.project.organization.domain.dto.OrgDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
@Api(tags = "그룹 정보")
public class OrganizationController {
    private final GroupService groupService;

    @ApiOperation(value = "그룹 전체 리스트")
    @GetMapping
    public ResponseEntity<OrgListResponseMessage> findAll(){
        List<OrgResponse> data = groupService.findAll(AccountUtil.getAccount());
        return new ResponseEntity<>(OrgListResponseMessage.of(StatusEnum.GROUP_OK, GROUP_FIND_SUCCESS, data), HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 카테고리 리스트")
    @GetMapping("/category/{category}")
    public ResponseEntity<OrgListResponseMessage> findByCategory(@PathVariable("category") String category){
        List<OrgResponse> data = groupService.findByCategory(category, AccountUtil.getAccount());
        return new ResponseEntity<>(OrgListResponseMessage.of(StatusEnum.GROUP_OK, GROUP_FIND_SUCCESS, data), HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 디테일 페이지")
    @GetMapping("/detail/{id}")
    public ResponseEntity<OrgDetailMessage> detailGroup(@PathVariable("id") Long id){
        OrgDetailResponse data = groupService.detailGroup(id);
        return new ResponseEntity<>(OrgDetailMessage.of(StatusEnum.GROUP_OK, GROUP_FIND_DETAIL_PAGE_SUCCESS, data),HttpStatus.OK);
    }


}
