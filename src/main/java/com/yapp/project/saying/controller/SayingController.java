package com.yapp.project.saying.controller;

import com.yapp.project.saying.domain.Saying;
import com.yapp.project.saying.domain.dto.SayingDto;
import com.yapp.project.saying.service.SayingService;
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
        return sayingService.randomSaying();
    }

    @ApiOperation(value = "오늘의 명언 제대로 썼는지 확인", tags = "saying-controller")
    @PostMapping("/check")
    public SayingDto.SayingResponse checkResult(SayingDto.SayingAccess request){
        return sayingService.checkResult(request);
    }
}
