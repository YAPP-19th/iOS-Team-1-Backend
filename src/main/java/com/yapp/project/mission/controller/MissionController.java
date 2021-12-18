package com.yapp.project.mission.controller;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.mission.domain.dto.MissionDto;
import com.yapp.project.mission.service.MissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mission")
@Api(tags = "미션 정보")
public class MissionController {
    private final MissionService missionService;

    @ApiOperation(value = "미션 생성")
    @PostMapping
    public Message createMission(@RequestBody MissionDto.MissionRequest request){
        return missionService.createMission(request, AccountUtil.getAccount());
    }

    @ApiOperation(value = "내 미션 리스트")
    @GetMapping
    public MissionDto.MissionResponseMessage findAllisDoing(){
        return missionService.findAllIsDoing(AccountUtil.getAccount());
    }

    @ApiOperation(value="종료된 내 미션 리스트")
    @GetMapping("/finish")
    public MissionDto.MissionResponseMessage findAllAlreadyFinish(){
        return missionService.findAllAlreadyFinish(AccountUtil.getAccount());
    }

    @ApiOperation(value = "미션 디테일 페이지")
    @GetMapping("/{id}")
    public MissionDto.MissionDetailResponseMessage findDetailMyMission(@PathVariable Long id){
        return missionService.findDetailMyMission(AccountUtil.getAccount(), id);
    }

    @ApiOperation(value = "미션 삭제")
    @DeleteMapping("/{id}")
    public Message deleteMyMission(@PathVariable Long id){
        return missionService.deleteMyMission(id);
    }

}
