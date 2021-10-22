package com.yapp.project.account.controller;

import static com.yapp.project.account.domain.dto.AccountDto.*;
import static com.yapp.project.aux.content.AccountContent.ACCOUNT_OK_MSG;

import com.yapp.project.account.service.AccountService;
import com.yapp.project.aux.StatusEnum;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    @ApiOperation(value = "회원정보", tags = "account-controller")
    @GetMapping("/me")
    public UserResponseMessage getMyAccountInfo() {
        UserResponse data = accountService.getUserInfo();
        return UserResponseMessage.of(StatusEnum.ACCOUNT_OK, ACCOUNT_OK_MSG, data);
    }
}
