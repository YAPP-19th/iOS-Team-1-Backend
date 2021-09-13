package com.yapp.project.account.controller;

import com.yapp.project.account.domain.dto.AccountResponseDto;
import com.yapp.project.account.service.AccountService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @ApiOperation(value = "회원정보", tags = "account-controller")
    @GetMapping("/me")
    public ResponseEntity<AccountResponseDto> getMyAccountInfo() {
        return ResponseEntity.ok(accountService.getUserInfo());
    }

    @ApiOperation(value = "이메일로 회원정보 보기", tags = "account-controller")
    @GetMapping("/{email}")
    public ResponseEntity<AccountResponseDto> getAccountInfo(@PathVariable String email){
        return ResponseEntity.ok(accountService.getAccountInfo(email));
    }

}
