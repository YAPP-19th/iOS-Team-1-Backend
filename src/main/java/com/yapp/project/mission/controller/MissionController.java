package com.yapp.project.mission.controller;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.mission.domain.dto.MissionDto;
import com.yapp.project.mission.service.MissionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mission")
public class MissionController {
    private final MissionService missionService;

    @ApiOperation(value = "미션 생성", tags = "mission-controller")
    @PostMapping
    public Message createMission(MissionDto.MissionRequest request){
        return missionService.createMission(request, AccountUtil.getAccount());
    }

    @ApiOperation(value = "내 미션 리스트", tags = "mission-controller")
    @GetMapping
    public MissionDto.MissionResponseMessage findAllisDoing(){
        return missionService.findAllIsDoing(AccountUtil.getAccount());
    }

    @ApiOperation(value = "미션 디테일 페이지", tags = "mission-controller")
    @GetMapping("/{id}")
    public MissionDto.MissionDetailResponseMessage findDetailMyMission(@PathVariable Long id){
        return missionService.findDetailMyMission(AccountUtil.getAccount(), id);
    }

    @ApiOperation(value = "미션 삭제", tags = "mission-controller")
    @DeleteMapping("/{id}")
    public Message deleteMyMission(@PathVariable Long id){
        return missionService.deleteMyMission(id);
    }

}
