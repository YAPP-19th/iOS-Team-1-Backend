package com.yapp.project.mission.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.content.MissionContent;
import com.yapp.project.config.exception.mission.AlreadyMissionExistException;
import com.yapp.project.config.exception.mission.MissionNotFoundException;
import com.yapp.project.mission.domain.Cron;
import com.yapp.project.mission.domain.Mission;
import static com.yapp.project.mission.domain.dto.MissionDto.*;

import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import com.yapp.project.routine.domain.Week;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {
    private final MissionRepository missionRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public Message createMission(MissionRequest request, Account account){
        if (missionRepository.findMissionByAccountAndOrganization_IdAndIsFinishIsFalseAndIsDeleteIsFalse(account, request.getId()).isPresent()){
            throw new AlreadyMissionExistException();
        }
        Organization organization = organizationRepository.getById(request.getId());
        Mission mission = request.toMission(account,organization);
        missionRepository.save(mission);
        setDays(request.getWeeks(),mission);
        return Message.of(StatusEnum.MISSION_OK, MissionContent.MISSION_CREATE_SUCCESS);
    }

    @Transactional(readOnly = true)
    public MissionResponseMessage findAllIsDoing(Account account) {
        List<MissionResponse> responses = new ArrayList<>();
        for (Mission mission : missionRepository.findAllByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(account)) {
            responses.add(mission.toMissionResponse());
        }
        return MissionResponseMessage.of(StatusEnum.MISSION_OK, MissionContent.FIND_MY_MISSION_LISTS_ING, responses);
    }

    @Transactional(readOnly = true)
    public MissionResponseMessage findAllAlreadyFinish(Account account){
        List<MissionResponse> responses = new ArrayList<>();
        for (Mission mission : missionRepository.findAllByAccountAndIsDeleteIsFalseAndIsFinishIsTrue(account)){
            responses.add(mission.toMissionResponse());
        }
        return MissionResponseMessage.of(StatusEnum.MISSION_OK, MissionContent.FIND_MY_MISSION_LISTS_FINISH, responses);
    }

    @Transactional(readOnly = true)
    public MissionDetailResponseMessage findDetailMyMission(Account account, Long missionId) {
        Mission mission = missionRepository.findMissionByAccountAndId(account, missionId)
                .orElseThrow(MissionNotFoundException::new);
        MissionDetailResponse response = mission.toMissionDetailResponse();
        return MissionDetailResponseMessage.of(StatusEnum.MISSION_OK, MissionContent.FIND_MY_MISSION_DETAIL, response);
    }

    @Transactional
    public Message deleteMyMission(Long missionId){
        Mission mission = missionRepository.findById(missionId).orElseThrow(MissionNotFoundException::new);
        missionRepository.delete(mission);
        return Message.of(StatusEnum.MISSION_OK, MissionContent.MISSION_DELETE_SUCCESS);
    }

    public List<Mission> checkLastDayMission(){
        List<Mission> missions = missionRepository.findAllByIsDeleteIsFalse();
        return missions.stream()
                .filter(mission -> mission.getAccount().getIsAlarm() &&
                        mission.getFinishDate().isEqual(DateUtil.KST_LOCAL_DATE_NOW()))
                .collect(Collectors.toList());
    }

    public List<Mission> getWakeUpClockMission(LocalDateTime dateTime){
        List<Mission> missions =  missionRepository.findAllByIsDeleteIsFalseAndIsAlarmIsTrueAndStartTimeEquals(dateTime.toLocalTime());
        List<Mission> response = new ArrayList<>();
        for (Mission mission : missions){
            Account account = mission.getAccount();
            if (account.getIsAlarm()){
                findTodayMission(mission, dateTime, response);
            }
        }
        return response;
    }

    private void findTodayMission(Mission mission, LocalDateTime dateTime, List<Mission> response) {
        for (Cron cron : mission.getWeeks()){
            if (cron.getWeek().getIndex() == dateTime.getDayOfWeek().getValue()){
                response.add(mission);
                break;
            }
        }
    }

    private void setDays(List<Week> days, Mission mission){
        List<Cron> missionDays = days.stream()
                .map(day -> Cron.builder().week(day).mission(mission).build()).collect(Collectors.toList());
        mission.addDays(missionDays);
    }
}

