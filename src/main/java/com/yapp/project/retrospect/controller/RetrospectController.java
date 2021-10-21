package com.yapp.project.retrospect.controller;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.retrospect.domain.dto.RetrospectDTO;
import com.yapp.project.retrospect.service.RetrospectService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/retrospect")
public class RetrospectController {

    private final RetrospectService retrospectService;

    @ApiOperation(value = "회고 추가", notes = "json이 아닌 multipart/form-data로 보내주서야 합니다.")
    @PostMapping("/")
    public RetrospectDTO.RequestRetrospectMessage createRetrospect(RetrospectDTO.RequestRetrospect retrospect) throws IOException {
        String imagePath;
        if(retrospect.getImage() == null) imagePath = null;
        else imagePath = retrospectService.saveImages(retrospect.getImage(), retrospect.getRoutineId());
        return retrospectService.createRetrospect(retrospect, imagePath, AccountUtil.getAccount());
    }

    @ApiOperation(value = "회고 수정", notes = "json이 아닌 multipart/form-data로 보내주서야 합니다.")
    @PatchMapping("/")
    public RetrospectDTO.RequestRetrospectMessage updateRetrospect(RetrospectDTO.RequestUpdateRetrospect retrospect) throws IOException {
        return retrospectService.updateRetrospect(retrospect, AccountUtil.getAccount());
    }

    @ApiOperation(value = "회고 삭제", notes = "삭제하려는 회고의 ID를 path로 넣어주세요.")
    @DeleteMapping("/{retrospectId}")
    public Message deleteRetrospect(@PathVariable Long retrospectId) {
        return retrospectService.deleteRetrospect(retrospectId, AccountUtil.getAccount());
    }

    @ApiOperation(value = "회고 단일 조회", notes = "조회하려는 회고의 ID를 path로 넣어주세요.")
    @GetMapping("/{retrospectId}")
    public RetrospectDTO.RequestRetrospectMessage getRetrospect(@PathVariable Long retrospectId) {
        return retrospectService.getRetrospect(retrospectId, AccountUtil.getAccount());
    }

}
