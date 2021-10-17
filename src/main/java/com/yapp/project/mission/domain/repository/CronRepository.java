package com.yapp.project.mission.domain.repository;

import com.yapp.project.mission.domain.Cron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CronRepository extends JpaRepository<Cron,Long> {
}
