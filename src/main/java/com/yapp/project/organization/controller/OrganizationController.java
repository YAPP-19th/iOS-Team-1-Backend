package com.yapp.project.organization.controller;

import com.yapp.project.aux.Message;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/group")
public class OrganizationController {

    @ApiOperation(value = "회원정보", tags = "organization-controller")
    @GetMapping
    public ResponseEntity<Message> findAll(){
        return ResponseEntity.ok(new Message());
    }
}
