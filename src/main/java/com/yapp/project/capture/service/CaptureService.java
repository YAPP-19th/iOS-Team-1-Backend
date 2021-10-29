package com.yapp.project.capture.service;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.capture.domain.CaptureImage;
import com.yapp.project.capture.domain.repository.CaptureImageRepository;
import com.yapp.project.config.exception.capture.AlreadyExistsCaptureException;
import com.yapp.project.config.exception.mission.MissionNotFoundException;
import com.yapp.project.capture.domain.Achievement;
import com.yapp.project.capture.domain.Capture;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.capture.domain.repository.CaptureRepository;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.yapp.project.aux.common.DateUtil.MID_NIGHT;
import static com.yapp.project.capture.domain.dto.CaptureDto.*;
import static com.yapp.project.aux.content.CaptureContent.*;

@Service
@RequiredArgsConstructor
public class CaptureService {
    private final CaptureRepository captureRepository;
    private final MissionRepository missionRepository;
    private final CaptureImageRepository captureImageRepository;


    @Transactional
    public CaptureResponseMessage captureTodayMission(String imagePath, Long missionId) {
        LocalDateTime todayMidnight = MID_NIGHT();
        Capture todayCapture = captureRepository.findByCreatedAtIsAfterAndMission_Id(todayMidnight, missionId).orElse(null);
        if(todayCapture!=null){
            throw new AlreadyExistsCaptureException();
        }
        Mission mission = missionRepository.findById(missionId).orElseThrow(MissionNotFoundException::new);
        mission.updateSuccessCount();
        Capture capture = saveCapture(mission, imagePath);
        captureRepository.save(capture);
        CaptureSuccessResponse data = CaptureSuccessResponse.builder().result(true).build();
        return CaptureResponseMessage.of(StatusEnum.CAPTURE_OK, CAPTURE_OK, data);
    }


    @Transactional(readOnly = true)
    public CaptureListResponseMessage getMyMissionImages(Long missionId, int page, int size, int recent){
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("createdAt").descending());
        List<Capture> captures = getCaptureLists(pageRequest, missionId, recent);
        List<CaptureResponse> captureResponses = getCaptureResponseLists(captures);
        CaptureListResponse data = CaptureListResponse.builder().captures(captureResponses).build();
        return CaptureListResponseMessage.of(StatusEnum.CAPTURE_OK, CAPTURE_LIST_SUCCESS, data);
    }


    @Transactional
    public CaptureResponseMessage deleteCaptureImages(DeleteIdListRequest request){
        List<Long> captureIds = request.getCaptureIdLists();
        List<Capture> captures = captureRepository.findCapturesByIdIn(captureIds).orElse(null);
        removeCaptureImageLists(captures);
        CaptureSuccessResponse data = CaptureSuccessResponse.builder().result(true).build();
        return CaptureResponseMessage.of(StatusEnum.CAPTURE_OK, CAPTURE_DELETE_SUCCESS, data);
    }

    @Transactional(readOnly = true)
    public CaptureListResponseMessage getOrganizationImages(Long organizationId, int page, int size, int recent){
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("createdAt").descending());
        List<Capture> captures = getCaptureListsAboutOrganization(pageRequest, organizationId, recent);
        List<CaptureResponse> captureResponses = getCaptureResponseLists(captures);
        CaptureListResponse data = CaptureListResponse.builder().captures(captureResponses).build();
        return CaptureListResponseMessage.of(StatusEnum.CAPTURE_OK, CAPTURE_LIST_SUCCESS, data);
    }


    private void removeCaptureImageLists(List<Capture> captures){
        if (captures!=null){
            for (Capture capture : captures){
                capture.remove();
                List<CaptureImage> images = capture.getCaptureImage();
                captureImageRepository.deleteAllInBatch(images);
            }
        }
    }


    private Capture saveCapture(Mission mission, String imagePath){
        int currentRank = mission.getOrganization().getCount();
        Integer myRank = currentRank+1;
        Achievement achievement = Achievement.of(mission);
        Organization organization = updateOrganizationInformation(mission);
        Capture capture = Capture.builder().achievement(achievement).mission(mission)
                .organization(organization).rank(myRank).build();
        CaptureImage image = CaptureImage.builder().capture(capture).url(imagePath).build();
        capture.updateCaptureImage(image);
        return capture;
    }


    private Organization updateOrganizationInformation(Mission mission){
        Organization organization = mission.getOrganization();
        organization.updateCurrentCount();
        return organization;
    }


    private List<Capture> getCaptureLists(Pageable pageRequest, Long missionId, int recent){
        if(recent == -1){
            return captureRepository.findByMission_IdAndIsDeleteIsFalse(pageRequest, missionId).orElse(null);
        }else{
            return captureRepository.findByMission_IdAndIsDeleteIsFalseOrderByCreatedAtDesc(pageRequest, missionId).orElse(null);
        }
    }


    private List<Capture> getCaptureListsAboutOrganization(Pageable pageRequest, Long organizationId, int recent){
        if(recent == -1){
            return captureRepository.findByOrganization_IdAndIsDeleteIsFalseOrderByCreatedAt(pageRequest, organizationId).orElse(null);
        }else{
            return captureRepository.findByOrganization_IdAndIsDeleteIsFalseOrderByCreatedAtDesc(pageRequest, organizationId).orElse(null);
        }
    }


    private List<CaptureResponse> getCaptureResponseLists(List<Capture> captures){
        if (captures!=null){
            return captures.stream()
                    .map(Capture::toCaptureResponse).collect(Collectors.toList());
        }else{
            return Collections.emptyList();
        }
    }

}
