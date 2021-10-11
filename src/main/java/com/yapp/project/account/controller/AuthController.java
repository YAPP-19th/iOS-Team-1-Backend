package com.yapp.project.account.controller;

import com.yapp.project.account.domain.dto.*;
import com.yapp.project.account.service.AuthService;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.note.account.AccountNotes;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @ApiOperation(value = "회원가입", tags = "auth-controller")
    @PostMapping("/signup")
    public ResponseEntity<Message> signup(@RequestBody AccountDto.UserRequest request){
        return new ResponseEntity<>(
                Message.builder().status(StatusEnum.OK).msg("회원가입 축하드립니다.")
                        .data(authService.normalSignup(request)).build(),
                HttpStatus.OK);
    }

    @ApiOperation(value = "소셜 로그인 접근", tags = "auth-controller", notes = AccountNotes.SOCIAL_NOTES)
    @PostMapping("/social")
    public ResponseEntity<Message> socialAccess(@RequestBody SocialDto.SocialRequest socialRequestDto){
        return new ResponseEntity<>(authService.socialAccess(socialRequestDto),HttpStatus.OK);
    }

    @ApiOperation(value = "소셜 회원가입 및 로그인", tags = "auth-controller", notes = AccountNotes.SOCIAL_SIGNUP_NOTES)
    @PostMapping("/social/signup")
    public ResponseEntity<Message> socialSignUp(@RequestBody SocialDto.SocialSignUpRequest requestDto){
        return new ResponseEntity<>(authService.socialSignUp(requestDto),HttpStatus.OK);
    }

    @ApiOperation(value = "로그인", tags = "auth-controller")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody AccountDto.UserRequest request){
        return ResponseEntity.ok(authService.normalLogin(request));
    }

    @ApiOperation(value = "로그아웃", tags = "auth-controller")
    @GetMapping("/logout")
    public ResponseEntity<Message> logout(){
        return ResponseEntity.ok(authService.logout());
    }

    @ApiOperation(value = "토큰 재발행", tags = "auth-controller")
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @ApiOperation(value = "닉네임 중복 확인", tags = "auth-controller")
    @GetMapping("/check/{name}")
    public ResponseEntity<Message> existByNickname(@PathVariable("name") String nickname ){
        return ResponseEntity.ok(authService.existByNickname(nickname));
    }
}
