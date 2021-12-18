package com.yapp.project.capture.controller;

import com.yapp.project.aux.note.capture.CaptureNotes;
import com.yapp.project.capture.service.CaptureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.yapp.project.capture.domain.dto.CaptureDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/capture")
@Api(tags = "사진 정보")
public class CaptureController {
    private final CaptureService captureService;

    @Value("${property.image.path}")
    private String path;

    @ApiOperation(value = "오늘의 미션 인증 처리", tags = "미션_관련_사진")
    @PostMapping
    public CaptureResponseMessage captureTodayMission(@RequestBody CaptureRequest request) throws IOException {
       return captureService.captureTodayMission(request);
    }

    @ApiOperation(value = "끝난 미션 관련 나의 이미지들 삭제하기", tags = "미션_관련_사진")
    @DeleteMapping
    public CaptureResponseMessage deleteCaptureImages(@RequestBody DeleteIdListRequest request){
        return captureService.deleteCaptures(request);
    }

    @ApiOperation(value = "미션 관련 나의 이미지들 가져오기", tags = "미션_관련_사진", notes = CaptureNotes.GET_CAPTURE_NOTES)
    @GetMapping("/mission/{missionId}")
    public CaptureListResponseMessage getMyMissionImages(@PathVariable Long missionId,
                                                     @RequestParam int page,@RequestParam int size,@RequestParam Integer recent){
        if (recent==null){
            recent = 1;
        }
        return captureService.getMyMissionImages(missionId, page, size, recent);
    }


    @ApiOperation(value = "그룹 이미지들 가져오기", tags = "그룹_관련_사진", notes = CaptureNotes.GET_CAPTURE_NOTES)
    @GetMapping("/org/{organizationId}")
    public CaptureListResponseMessage getOrganizationImages(@PathVariable Long organizationId,
                                                         @RequestParam int page,@RequestParam int size,@RequestParam Integer recent){
        if (recent==null){
            recent = 1;
        }
        return captureService.getOrganizationImages(organizationId, page, size, recent);
    }


}
