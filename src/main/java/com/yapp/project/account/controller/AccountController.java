package com.yapp.project.account.controller;

import com.yapp.project.account.domain.dto.AccountDto;
import com.yapp.project.account.service.AccountService;
import com.yapp.project.aux.Message;
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

    @ApiOperation(value = "닉네임 중복 확인", tags = "account-controller")
    @PostMapping("/check/nickname")
    public ResponseEntity<Message> existByNickname(@RequestBody AccountDto.NicknameRequest requestDto){
        return ResponseEntity.ok(accountService.existByNickname(requestDto));
    }

}
