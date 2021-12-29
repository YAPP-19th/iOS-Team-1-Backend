package com.yapp.project.capture.domain.repository;

import com.yapp.project.capture.domain.Capture;
import com.yapp.project.mission.domain.Mission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface CaptureRepository extends JpaRepository<Capture, Long> {
    Capture findFirstByMissionOrderByCreatedAtDesc(Mission mission);
    Optional<List<Capture>> findByMission_IdAndIsDeleteIsFalse(Pageable pageRequest, Long missionId);
    Optional<List<Capture>> findByMission_IdAndIsDeleteIsFalseOrderByCreatedAtDesc(Pageable pageRequest, Long missionId);
    Optional<Capture> findByCreatedAtIsAfterAndMission_Id(LocalDateTime date, Long mission);
    Optional<List<Capture>> findByOrganization_IdAndIsDeleteIsFalseOrderByCreatedAtDesc(Pageable pageRequest, Long organizationId);
    Optional<List<Capture>> findByOrganization_IdAndIsDeleteIsFalseOrderByCreatedAt(Pageable pageRequest, Long organizationId);
    Optional<List<Capture>> findCapturesByIdIn(List<Long> ids);
    List<Capture> findAllByMission(Mission mission);
}
