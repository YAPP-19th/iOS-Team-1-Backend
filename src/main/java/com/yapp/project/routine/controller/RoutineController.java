package com.yapp.project.routine.controller;

import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routine")
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping("/")
    public RoutineDTO.Response createRoutine(@RequestBody RoutineDTO.Request newRoutine) throws BindException {
        return routineService.createRoutine(newRoutine);
    }
}
