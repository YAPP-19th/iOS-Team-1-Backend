package com.yapp.project.mission.domain.repository;

import com.yapp.project.account.domain.Account;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.dao.MissionOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission,Long> {
    ArrayList<MissionOrganization> findMissionByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(Account account);
    List<Mission> findAllByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(Account account);
    List<Mission> findAllByAccountAndIsDeleteIsFalseAndIsFinishIsTrue(Account account);
    List<Mission> findAllByIsDeleteIsFalseAndIsAlarmIsTrueAndStartTimeEquals(LocalTime startTime);
    List<Mission> findAllByIsDeleteIsFalse();
    Optional<Mission> findMissionByAccountAndId(Account account, Long missionId);
    Optional<Mission> findMissionByAccountAndOrganization_IdAndIsFinishIsFalseAndIsDeleteIsFalse(Account account, Long organizationId);
}
