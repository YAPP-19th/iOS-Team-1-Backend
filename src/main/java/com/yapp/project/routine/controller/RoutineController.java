package com.yapp.project.routine.controller;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.domain.Week;
import com.yapp.project.routine.service.RoutineService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/routine")
@Api(tags = "루틴")
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
    @GetMapping("/day/{date}")
    public RoutineDTO.ResponseRoutineDateListMessageDto getRoutineList(@PathVariable String date) {
        return routineService.getRoutineList(date, AccountUtil.getAccount());
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

    @ApiOperation(value = "요일 루틴 순서 편집", notes = "요일 루틴 순서 편집하기\n 루틴ID를 순서대로 넘겨주세요")
    @PatchMapping("/sequence/{day}")
    public Message updateRoutineSequence(
            @PathVariable Week day, @RequestBody RoutineDTO.RequestRoutineSequence sequence) {
        return routineService.updateRoutineSequence(day, sequence.getSequence(), AccountUtil.getAccount());
    }

    @ApiOperation(value = "주단위 일별 루틴 수행률 조회", notes = "주단위로 일별 루티 수행률 조회하기\n 요청 요일은 월요일이어야 합니다." +
            " \n 월요일부터 일요일 까지 계산되어 제공됩니다.")
    @GetMapping("/{start}/rate")
    public RoutineDTO.ResponseDaysRoutineRateMessageDto getDaysRoutineRate(@PathVariable String start) {
        return routineService.getRoutineDaysRate(AccountUtil.getAccount(), LocalDate.parse(start));
    }

    @ApiOperation(value = "추천 루틴 조회", notes = "추천 루틴 조회하기")
    @GetMapping("/recommended")
    public RoutineDTO.ResponseRecommendedRoutineMessageDto getRecommendedRoutine() {
        return routineService.getRecommendedRoutine();
    }
}