package com.yapp.project.routine.controller;

import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.domain.Week;
import com.yapp.project.routine.service.RoutineService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routine")
public class RoutineController {

    private final RoutineService routineService;

    @ApiOperation(value = "루틴 추가", notes = "새로운 루틴 추가하기")
    @PostMapping("/")
    public RoutineDTO.ResponseRoutineDto createRoutine(@RequestBody RoutineDTO.RequestRoutineDto newRoutine) throws BindException {
        return routineService.createRoutine(newRoutine, AccountUtil.getAccount());
    }

    @GetMapping("/{routineId}")
    public RoutineDTO.ResponseRoutineDto getRoutine(@PathVariable Long routineId) throws BindException {
        return routineService.getRoutine(routineId, AccountUtil.getAccount());
    }

    @GetMapping("/day/{day}")
    public List<RoutineDTO.ResponseRoutineDto> getRoutineList(@PathVariable Week day) {
        return routineService.getRoutineList(day, AccountUtil.getAccount());
    }
}