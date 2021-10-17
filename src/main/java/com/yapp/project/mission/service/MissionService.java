package com.yapp.project.mission.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.mission.AlreadyMissionExistException;
import com.yapp.project.config.exception.mission.MissionNotFoundException;
import com.yapp.project.mission.domain.Cron;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.dto.MissionDto;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import com.yapp.project.routine.domain.Week;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {
    private final MissionRepository missionRepository;
    private final OrganizationRepository organizationRepository;

    public Message createMission(MissionDto.MissionRequest request, Account account){
        if (missionRepository.findMissionByAccountAndOrganization_IdAndIsFinishIsFalse(account, request.getId()).isPresent()){
            throw new AlreadyMissionExistException();
        }
        Organization organization = organizationRepository.getById(request.getId());
        Mission mission = request.toMission(account,organization);
        setDays(request.getWeeks(),mission);
        missionRepository.save(mission);
        return Message.of(StatusEnum.MISSION_OK,"미션 생성 성공");
    }

    private void setDays(List<Week> days, Mission mission){
        List<Cron> missionDays = days.stream().map(day -> Cron.builder().week(day).mission(mission).build()).collect(Collectors.toList());
        mission.addDays(missionDays);
    }


    public MissionDto.MissionResponseMessage findAllIsDoing(Account account) {
        List<MissionDto.MissionResponse> responses = new ArrayList<>();
        for (Mission mission : missionRepository.findAllByAccountAndIsFinishIsFalse(account)) {
            responses.add(mission.toMissionResponse());
        }
        return MissionDto.MissionResponseMessage.builder()
                .message(
                        Message.builder().msg("진행중인 나의 미션 가져오기 성공").status(StatusEnum.MISSION_OK).build()
                ).data(responses).build();
    }


    public MissionDto.MissionDetailResponseMessage findDetailMyMission(Account account, Long missionId) {
        Mission mission = missionRepository.findMissionByAccountAndId(account, missionId).orElseThrow(MissionNotFoundException::new);
        MissionDto.MissionDetailResponse response = mission.toMissionDetailResponse();
        return MissionDto.MissionDetailResponseMessage.builder().message(
                Message.builder().msg("나의 디테일 미션 페이지 가져오기 성공").status(StatusEnum.MISSION_OK).build()
        ).data(response).build();
    }
}

