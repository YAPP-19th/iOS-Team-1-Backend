package com.yapp.project.account.controller;

import com.yapp.project.account.domain.dto.AccountRequestDto;
import com.yapp.project.account.domain.dto.TokenDto;
import com.yapp.project.account.domain.dto.TokenRequestDto;
import com.yapp.project.account.service.AuthService;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @ApiOperation(value = "회원가입", tags = "auth-controller")
    @PostMapping("/signup")
    public ResponseEntity<Message> signup(@RequestBody AccountRequestDto accountRequestDto){
        return new ResponseEntity<>(
                Message.builder().status(StatusEnum.OK).msg("회원가입 축하드립니다.")
                        .data(authService.signup(accountRequestDto)).build(),
                HttpStatus.OK);
    }

    @ApiOperation(value = "로그인", tags = "auth-controller")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody AccountRequestDto accountRequestDto){
        return ResponseEntity.ok(authService.login(accountRequestDto));
    }

    @ApiOperation(value = "토큰 재발행", tags = "auth-controller")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}
