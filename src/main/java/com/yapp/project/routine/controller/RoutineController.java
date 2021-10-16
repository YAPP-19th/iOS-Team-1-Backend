package com.yapp.project.routine.controller;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.domain.Week;
import com.yapp.project.routine.service.RoutineService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routine")
public class RoutineController {

    private final RoutineService routineService;

    @ApiOperation(value = "루틴 추가", notes = "새로운 루틴 추가하기")
    @PostMapping("/")
    public RoutineDTO.ResponseRoutineMessageDto createRoutine(@RequestBody RoutineDTO.RequestRoutineDto newRoutine) {
        return routineService.createRoutine(newRoutine, AccountUtil.getAccount());
    }

    @ApiOperation(value = "루틴 단일 조회", notes = "루틴ID로 루틴 조회하기")
    @GetMapping("/{routineId}")
    public RoutineDTO.ResponseRoutineMessageDto getRoutine(@PathVariable Long routineId) {
        return routineService.getRoutine(routineId, AccountUtil.getAccount());
    }

    @ApiOperation(value = "루틴 요일별 전체 조회", notes = "요일별 루틴 전체 조회하기")
    @GetMapping("/day/{day}")
    public RoutineDTO.ResponseRoutineListMessageDto getRoutineList(@PathVariable Week day) {
        return routineService.getRoutineList(day, AccountUtil.getAccount());
    }

    @ApiOperation(value = "루틴 수정", notes = "루틴 수정하기")
    @PatchMapping("/{routineId}")
    public RoutineDTO.ResponseRoutineMessageDto updateRoutine(@PathVariable Long routineId, @RequestBody RoutineDTO.RequestRoutineDto updateRoutine) {
        return routineService.updateRoutine(routineId, updateRoutine, AccountUtil.getAccount());
    }

    @ApiOperation(value = "루틴 삭제", notes = "루틴 삭제하기")
    @DeleteMapping("/{routineId}")
    public Message deleteRoutine(@PathVariable Long routineId) {
        return routineService.deleteRoutine(routineId, AccountUtil.getAccount());
    }

    @PatchMapping("/sequence/{day}")
    public RoutineDTO.ResponseRoutineListMessageDto updateRoutineSequence(
            @PathVariable Week day, @RequestBody ArrayList<Long> sequence) {
        return routineService.updateRoutineSequence(day, sequence, AccountUtil.getAccount());
    }
}