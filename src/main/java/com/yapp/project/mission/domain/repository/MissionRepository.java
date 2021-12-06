package com.yapp.project.mission.domain.repository;

import com.yapp.project.account.domain.Account;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.dao.MissionOrganization;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission,Long> {
    @EntityGraph(attributePaths = {"organization"})
    @Query("SELECT DISTINCT m FROM Mission m WHERE m.account = ?1 and m.isFinish = false  and m.isDelete = false")
    ArrayList<MissionOrganization> findMissionByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(Account account);

    @EntityGraph(attributePaths = {"organization"})
    List<Mission> findAllByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(Account account);

    @EntityGraph(attributePaths = {"organization"})
    List<Mission> findAllByAccountAndIsDeleteIsFalseAndIsFinishIsTrue(Account account);

    @EntityGraph(attributePaths = {"organization"})
    List<Mission> findAllByIsDeleteIsFalseAndIsAlarmIsTrueAndStartTimeEquals(LocalTime startTime);

    @EntityGraph(attributePaths = {"organization"})
    List<Mission> findAllByIsDeleteIsFalse();

    @EntityGraph(attributePaths = {"organization"})
    List<Mission> findAllByIsDeleteIsFalseAndIsFinishIsFalse();

    @EntityGraph(attributePaths = {"organization"})
    List<Mission> findAllByAccount(Account account);

    Optional<Mission> findMissionByAccountAndId(Account account, Long missionId);
    Optional<Mission> findMissionByAccountAndOrganization_IdAndIsFinishIsFalseAndIsDeleteIsFalse(Account account, Long organizationId);
}
