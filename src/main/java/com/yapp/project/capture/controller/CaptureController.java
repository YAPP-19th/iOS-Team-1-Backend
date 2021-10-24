package com.yapp.project.capture.controller;

import com.yapp.project.capture.service.CaptureService;
import com.yapp.project.config.exception.capture.InvalidCaptureException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.yapp.project.aux.common.SnapShotUtil.saveImages;
import static com.yapp.project.capture.domain.dto.CaptureDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/capture")
public class CaptureController {
    private final CaptureService captureService;

    @ApiOperation(value = "오늘의 미션 인증 처리", tags = "capture-controller")
    @PostMapping("/today")
    public CaptureResponseMessage captureTodayMission(CaptureRequest request) throws IOException {
        MultipartFile image = request.getImage();
        Long missionId = request.getMissionId();
        String imagePath;
        if(image == null) {
            throw new InvalidCaptureException();
        } else{
            imagePath = saveImages(image, missionId,  "/home/vixx/image/capture/");
        }
        return captureService.captureTodayMission(imagePath, missionId);
    }

    @ApiOperation(value = "끝난 미션 관련 나의 이미지들 삭제하기", tags = "capture-controller")
    @PostMapping("/delete")
    public CaptureResponseMessage deleteCaptureImages(DeleteIdListRequest request){
        return captureService.deleteCaptureImages(request);
    }

    @ApiOperation(value = "미션 관련 나의 이미지들 가져오기", tags = "capture-controller")
    @GetMapping("/{missionId}")
    public CaptureListResponseMessage getMyMissionImages(@PathVariable Long missionId,
                                                     @RequestParam int page,@RequestParam int size){
        return captureService.getMyMissionImages(missionId, page, size);
    }


}