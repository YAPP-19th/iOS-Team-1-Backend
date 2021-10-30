package com.yapp.project.report.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthRoutineReportRepository extends JpaRepository<MonthRoutineReport, Long> {
}
