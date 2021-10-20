package com.yapp.project.saying.controller;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.common.AccountUtil;
import static com.yapp.project.saying.domain.dto.SayingDto.*;

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
    public SayingAccessMessage randomSaying(){
        int randomSayingId = SayingUtils.randomSayingId();
        Account account = AccountUtil.getAccount();
        return sayingService.randomSaying(account, randomSayingId);
    }

    @ApiOperation(value = "클라이언트에서 보내 준 오늘의 명언 제대로 썼는지 확인", tags = "saying-controller")
    @PostMapping("/check")
    public SayingResponseMessage checkResult(SayingAccess request){
        Account account = AccountUtil.getAccount();
        return sayingService.checkResult(request, account);
    }

    @ApiOperation(value = "명언 기록을 읽어서 오늘 썼는 지 확인", tags = "saying-controller")
    @GetMapping("/check")
    public SayingRecordResponseMessage isTodaySayingRecord(){
        Account account = AccountUtil.getAccount();
        return sayingService.isTodayRecording(account);
    }
}
