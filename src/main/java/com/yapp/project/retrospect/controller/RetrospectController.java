package com.yapp.project.retrospect.controller;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.retrospect.domain.dto.RetrospectDTO;
import com.yapp.project.retrospect.service.RetrospectService;
import com.yapp.project.routine.domain.Week;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDate;

import static com.yapp.project.aux.common.SnapShotUtil.saveImages;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/retrospect")
public class RetrospectController {

    private final RetrospectService retrospectService;

    @ApiOperation(value = "회고 추가", notes = "json이 아닌 multipart/form-data로 보내주서야 합니다.")
    @PostMapping("/")
    public RetrospectDTO.ResponseRetrospectMessage createRetrospect(RetrospectDTO.RequestRetrospect retrospect) throws IOException {
        String imagePath;
        if(retrospect.getImage() == null) {
            imagePath = null;
        } else{
            imagePath = saveImages(retrospect.getImage(), retrospect.getRoutineId(), "/home/image/retrospect/");
        }
        return retrospectService.createRetrospect(retrospect, imagePath, AccountUtil.getAccount());
    }

    @ApiOperation(value = "회고 수정", notes = "json이 아닌 multipart/form-data로 보내주서야 합니다.")
    @PatchMapping("/")
    public RetrospectDTO.ResponseRetrospectMessage updateRetrospect(RetrospectDTO.RequestUpdateRetrospect retrospect) throws IOException {
        return retrospectService.updateRetrospect(retrospect, AccountUtil.getAccount());
    }

    @ApiOperation(value = "회고 삭제", notes = "삭제하려는 회고의 ID를 path로 넣어주세요.")
    @DeleteMapping("/{retrospectId}")
    public Message deleteRetrospect(@PathVariable Long retrospectId) {
        return retrospectService.deleteRetrospect(retrospectId, AccountUtil.getAccount());
    }

    @ApiOperation(value = "회고 단일 조회", notes = "조회하려는 회고의 ID를 path로 넣어주세요.")
    @GetMapping("/{retrospectId}")
    public RetrospectDTO.ResponseRetrospectMessage getRetrospect(@PathVariable Long retrospectId) {
        return retrospectService.getRetrospect(retrospectId, AccountUtil.getAccount());
    }

    @ApiOperation(value = "회고 날짜 기준 전체 조회", notes = "조회하려는 날짜를 path로 넣어주세요. \n date: 2021-10-21")
    @GetMapping("/list/{date}")
    public RetrospectDTO.ResponseRetrospectListMessage getRetrospectList(@PathVariable String date) {
        return retrospectService.getRetrospectList(LocalDate.parse(date), AccountUtil.getAccount());
    }

    @ApiOperation(value = "루틴 수행여부 설정(회고에 적용)", notes = "완료: DONE, 부분완료: TRY, 취소: NOT / 여기서 취소는 수행여부 체크 이후 취소를 의미합니다.\n " +
            "돌아보기가 존재하면 아래 예시 데이터 대로 응답, 돌아보기가 존재하지 않으면 돌아보기 데이터는 빈 값으로 응답")
    @PostMapping("/result")
    public RetrospectDTO.ResponseRetrospectMessage setRetrospectResult(@RequestBody RetrospectDTO.RequestRetrospectResult result) {
        return retrospectService.setRetrospectResult(result, AccountUtil.getAccount());
    }
}
