package com.yapp.project.routine.controller;

import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.service.RoutineService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routine")
public class RoutineController {

    private final RoutineService routineService;

    @ApiOperation(value = "루틴 추가", notes = "새로운 루틴 추가하기")
    @PostMapping("/")
    public RoutineDTO.ResponseRoutineDto createRoutine(@RequestBody RoutineDTO.RequestRoutineDto newRoutine) throws BindException {
        return routineService.createRoutine(newRoutine);
    }
}