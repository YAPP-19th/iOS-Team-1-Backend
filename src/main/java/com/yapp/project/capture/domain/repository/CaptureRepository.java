package com.yapp.project.capture.domain.repository;

import com.yapp.project.capture.domain.Capture;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface CaptureRepository extends JpaRepository<Capture, Long> {
    Optional<List<Capture>> findByMission_IdAndIsDeleteIsFalse(Pageable pageRequest, Long missionId);
    Optional<Capture> findByCreatedAtIsAfterAndMission_Id(LocalDateTime date, Long mission);
    Optional<List<Capture>> findCapturesByIdIn(List<Long> ids);
}
