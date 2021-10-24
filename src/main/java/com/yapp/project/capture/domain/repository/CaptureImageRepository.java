package com.yapp.project.capture.domain.repository;

import com.yapp.project.capture.domain.CaptureImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaptureImageRepository extends JpaRepository<CaptureImage, Long> {
}
