package com.yapp.project.account.controller;

import com.yapp.project.account.domain.dto.AccountResponseDto;
import com.yapp.project.account.service.AccountService;
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

    @GetMapping("/me")
    public ResponseEntity<AccountResponseDto> getMyAccountInfo() {
        return ResponseEntity.ok(accountService.getUserInfo());
    }

    @GetMapping("/{email}")
    public ResponseEntity<AccountResponseDto> getAccountInfo(@PathVariable String email){
        return ResponseEntity.ok(accountService.getAccountInfo(email));
    }

}
