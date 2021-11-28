package com.yapp.project.capture.controller;

import com.yapp.project.aux.note.capture.CaptureNotes;
import com.yapp.project.capture.service.CaptureService;
import com.yapp.project.config.exception.capture.InvalidCaptureException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.yapp.project.aux.common.SnapShotUtil.saveImages;
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
    public CaptureResponseMessage captureTodayMission(CaptureRequest request) throws IOException {
        MultipartFile image = request.getImage();
        Long missionId = request.getMissionId();
        String imagePath;
        if(image == null) {
            throw new InvalidCaptureException();
        } else{
            imagePath = saveImages(image, missionId,  path+"capture/");
        }
        return captureService.captureTodayMission(imagePath, missionId);
    }

    @ApiOperation(value = "끝난 미션 관련 나의 이미지들 삭제하기", tags = "미션_관련_사진")
    @DeleteMapping
    public CaptureResponseMessage deleteCaptureImages(DeleteIdListRequest request){
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
