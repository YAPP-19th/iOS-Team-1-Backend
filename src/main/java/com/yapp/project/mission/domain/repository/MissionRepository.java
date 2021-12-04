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
    @Query("SELECT DISTINCT m FROM Mission m WHERE m.account = ?1")
    ArrayList<MissionOrganization> findMissionByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(Account account);

    @EntityGraph(attributePaths = {"organization"})
    @Query("SELECT DISTINCT m FROM Mission m ")
    List<Mission> findAllByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(Account account);

    @EntityGraph(attributePaths = {"organization"})
    @Query("SELECT DISTINCT m FROM Mission m ")
    List<Mission> findAllByAccountAndIsDeleteIsFalseAndIsFinishIsTrue(Account account);

    @EntityGraph(attributePaths = {"organization"})
    @Query("SELECT DISTINCT m FROM Mission m ")
    List<Mission> findAllByIsDeleteIsFalseAndIsAlarmIsTrueAndStartTimeEquals(LocalTime startTime);

    @EntityGraph(attributePaths = {"organization"})
    @Query("SELECT DISTINCT m FROM Mission m ")
    List<Mission> findAllByIsDeleteIsFalse();

    @EntityGraph(attributePaths = {"organization"})
    @Query("SELECT DISTINCT m FROM Mission m ")
    List<Mission> findAllByIsDeleteIsFalseAndIsFinishIsFalse();

    @EntityGraph(attributePaths = {"organization"})
    @Query("SELECT DISTINCT m FROM Mission m WHERE m.account = ?1")
    List<Mission> findAllByAccount(Account account);

    Optional<Mission> findMissionByAccountAndId(Account account, Long missionId);
    Optional<Mission> findMissionByAccountAndOrganization_IdAndIsFinishIsFalseAndIsDeleteIsFalse(Account account, Long organizationId);
}
