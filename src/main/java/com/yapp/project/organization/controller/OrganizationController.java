package com.yapp.project.organization.controller;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import static com.yapp.project.organization.domain.dto.OrgDto.*;
import com.yapp.project.organization.service.GroupService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
public class OrganizationController {
    private final GroupService groupService;

    @ApiOperation(value = "그룹 전체 리스트", tags = "organization-controller")
    @GetMapping
    public ResponseEntity<OrgListResponseMessage> findAll(){
        return new ResponseEntity<>(OrgListResponseMessage.builder()
                .data(groupService.findAll())
                .message(Message.builder().status(StatusEnum.OK).msg("조회 성공").build())
                .build(), HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 카테고리 리스트", tags = "organization-controller")
    @GetMapping("/category/{category}")
    public ResponseEntity<OrgListResponseMessage> findByCategory(@PathVariable("category") String category){
        return new ResponseEntity<>(OrgListResponseMessage.builder()
                .data(groupService.findByCategory(category))
                .message(Message.builder().status(StatusEnum.OK).msg("조회 성공").build())
                .build(), HttpStatus.OK);
    }

    @ApiOperation(value = "그룹 디테일 페이지",tags = "organization-controller")
    @GetMapping("/detail/{id}")
    public ResponseEntity<OrgDetailMessage> detailGroup(@PathVariable("id") Long id){
        return new ResponseEntity<>(OrgDetailMessage.builder()
                .data(groupService.detailGroup(id))
                .message(Message.builder().status(StatusEnum.OK).msg("디테일 페이지 접근 성공").build())
                .build(),HttpStatus.OK);
    }


}
