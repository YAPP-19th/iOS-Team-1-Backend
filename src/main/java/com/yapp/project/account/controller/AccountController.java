package com.yapp.project.account.controller;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.service.AccountService;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.common.AccountUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.yapp.project.account.domain.dto.AccountDto.*;
import static com.yapp.project.aux.content.AccountContent.ACCOUNT_OK_MSG;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@Api(tags = "유저 관련")
public class AccountController {
    private final AccountService accountService;

    @ApiOperation(value = "회원정보", tags = "유저 정보")
    @GetMapping("/me")
    public UserResponseMessage getMyAccountInfo() {
        UserResponse data = accountService.getUserInfo();
        return UserResponseMessage.of(StatusEnum.ACCOUNT_OK, ACCOUNT_OK_MSG, data);
    }

    @ApiOperation(value = "프로필에서 비밀번호 변경")
    @PostMapping
    public Message resetMyPassword(@RequestBody ProfilePasswordRequest request){
        Account account = AccountUtil.getAccount();
        return accountService.resetMyAccountPassword(request, account);
    }

    @ApiOperation(value = "유저정보 삭제")
    @DeleteMapping
    public Message removeAccount() {
        Account account = AccountUtil.getAccount();
        return accountService.removeAccount(account);
    }

    @ApiOperation(value = "알람토글 변경")
    @GetMapping
    public Message clickAlarmToggle(@RequestParam("is-on") Boolean isOn) {
        Account account = AccountUtil.getAccount();
        return accountService.clickAlarmToggle(account, isOn);
    }

    @ApiOperation(value = "프로필 사진 변경")
    @PostMapping("/profile")
    public Message changeProfileImage(ProfileImageRequest request) throws IOException {
        Account account = AccountUtil.getAccount();
        return accountService.changeProfileImage(account, request);
    }
}
