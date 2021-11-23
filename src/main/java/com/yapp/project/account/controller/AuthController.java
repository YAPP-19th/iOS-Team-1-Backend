package com.yapp.project.account.controller;

import com.yapp.project.account.domain.dto.*;
import com.yapp.project.account.service.AuthService;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.note.account.AccountNotes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Api(tags = "유저 관련", hidden = true)
public class AuthController {
    private final AuthService authService;

    @ApiOperation(value = "회원가입", tags = "일반 회원 절차")
    @PostMapping("/signup")
    public ResponseEntity<SocialDto.TokenMessage> signup(@RequestBody AccountDto.UserRequest request){
        return new ResponseEntity<>(authService.normalSignUp(request), HttpStatus.OK);
    }

    @ApiOperation(value = "소셜 로그인 접근", tags = "소셜 회원 절차", notes = AccountNotes.SOCIAL_NOTES)
    @PostMapping("/social")
    public ResponseEntity<SocialDto.SocialResponseMessage> socialAccess(@RequestBody SocialDto.SocialRequest socialRequestDto){
        return new ResponseEntity<>(authService.socialAccess(socialRequestDto),HttpStatus.OK);
    }

    @ApiOperation(value = "소셜 회원가입 및 로그인", tags = "소셜 회원 절차", notes = AccountNotes.SOCIAL_SIGNUP_NOTES)
    @PostMapping("/social/signup")
    public ResponseEntity<SocialDto.TokenMessage> socialSignUp(@RequestBody SocialDto.SocialSignUpRequest requestDto){
        return new ResponseEntity<>(authService.socialSignUp(requestDto),HttpStatus.OK);
    }

    @ApiOperation(value = "로그인", tags = "일반 회원 절차")
    @PostMapping("/login")
    public ResponseEntity<SocialDto.TokenMessage> login(@RequestBody AccountDto.LoginRequest request){
        return ResponseEntity.ok(authService.normalLogin(request));
    }

    @ApiOperation(value = "로그아웃", tags = "일반 회원 절차")
    @GetMapping("/logout")
    public ResponseEntity<Message> logout(){
        return ResponseEntity.ok(authService.logout());
    }

    @ApiOperation(value = "토큰 재발행", tags = "기타 유저 관련")
    @PostMapping("/reissue")
    public ResponseEntity<SocialDto.TokenMessage> reissue(@RequestBody TokenRequestDto tokenRequestDto){
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @ApiOperation(value = "이메일이 실제로 존재하는 지 확인 후 인증번호 보내기", tags = "비밀번호 재설정 관련")
    @PostMapping("/send")
    public Message sendAuthenticationNumber(AccountDto.EmailRequest request){
        return authService.sendAuthenticationNumber(request);
    }

    @ApiOperation(value = "인증번호가 실제로 일치하는 지 확인하는 작업", tags = "비밀번호 재설정 관련")
    @PostMapping("/check/number")
    public Message checkAuthenticationNumber(AccountDto.AuthenticationNumberRequest request){
        return authService.checkAuthenticationNumber(request);
    }

    @ApiOperation(value = "비밀번호 재설정하는 작업", tags = "비밀번호 재설정 관련")
    @PostMapping("/set/password")
    public Message resetPassword(AccountDto.ResetPasswordRequest request){
        return authService.resetPassword(request);
    }

    @ApiOperation(value = "이메일 존재 여부")
    @GetMapping("/email/{email}")
    public AccountDto.EmailValidationResponseMessage validateEmail(@PathVariable String email){
        return authService.isAlreadyExistEmail(email);
    }

}
