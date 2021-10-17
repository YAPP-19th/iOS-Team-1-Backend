package com.yapp.project.mission.domain.repository;

import com.yapp.project.mission.domain.Capture;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CaptureRepository extends JpaRepository<Capture, Long> {
}
