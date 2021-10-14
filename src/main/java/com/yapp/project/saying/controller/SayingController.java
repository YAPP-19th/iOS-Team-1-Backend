package com.yapp.project.saying.controller;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.saying.domain.Saying;
import com.yapp.project.saying.domain.dto.SayingDto;
import com.yapp.project.saying.service.SayingService;
import com.yapp.project.saying.utils.SayingUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/saying")
@RequiredArgsConstructor
public class SayingController {
    private final SayingService sayingService;

    @ApiOperation(value = "오늘의 사용자 명언", tags = "saying-controller")
    @GetMapping("/today")
    public Saying randomSaying(){
        Account account = AccountUtil.getAccount();
        int id = SayingUtils.randomSayingId();
        return sayingService.randomSaying(account, id);
    }

    @ApiOperation(value = "오늘의 명언 제대로 썼는지 확인", tags = "saying-controller")
    @PostMapping("/check")
    public SayingDto.SayingResponse checkResult(SayingDto.SayingAccess request){
        Account account = AccountUtil.getAccount();
        return sayingService.checkResult(request, account);
    }
}
