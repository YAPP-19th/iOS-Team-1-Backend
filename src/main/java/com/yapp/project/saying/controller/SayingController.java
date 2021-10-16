package com.yapp.project.saying.controller;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.common.AccountUtil;
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
    public SayingDto.SayingAccessMessage randomSaying(){
        return SayingDto.SayingAccessMessage.builder()
                .data(sayingService.randomSaying(AccountUtil.getAccount(), SayingUtils.randomSayingId()))
                .message(Message.builder().status(StatusEnum.SAYING_OK).msg("명언 가져오기 성공").build()).build() ;
    }

    @ApiOperation(value = "오늘의 명언 제대로 썼는지 확인", tags = "saying-controller")
    @PostMapping("/check")
    public SayingDto.SayingResponseMessage checkResult(SayingDto.SayingAccess request){
        return SayingDto.SayingResponseMessage.builder()
                .data(sayingService.checkResult(request, AccountUtil.getAccount()))
                .message(Message.builder().status(StatusEnum.SAYING_OK).msg("명언 확인 성공").build()).build();
    }
}
