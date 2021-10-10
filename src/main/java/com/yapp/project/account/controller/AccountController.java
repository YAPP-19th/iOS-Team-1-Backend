package com.yapp.project.account.controller;

import com.yapp.project.account.domain.dto.AccountDto;
import com.yapp.project.account.service.AccountService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @ApiOperation(value = "회원정보", tags = "account-controller")
    @GetMapping("/me")
    public ResponseEntity<AccountDto.Response> getMyAccountInfo() {
        return ResponseEntity.ok(accountService.getUserInfo());
    }
}
