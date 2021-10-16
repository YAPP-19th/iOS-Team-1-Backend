package com.yapp.project.account.controller;

import com.yapp.project.account.domain.dto.AccountDto;
import com.yapp.project.account.service.AccountService;
import com.yapp.project.aux.Message;
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
    public AccountDto.UserResponseMessage getMyAccountInfo() {
        return AccountDto.UserResponseMessage.builder().data(accountService.getUserInfo())
                .message(Message.builder().msg("성공").status(StatusEnum.ACCOUNT_OK).build()).build();
    }
}
