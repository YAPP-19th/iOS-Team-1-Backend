package com.yapp.project.report.domain;

import com.yapp.project.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthRoutineReportRepository extends JpaRepository<MonthRoutineReport, Long> {
    List<MonthRoutineReport> findAllByAccountAndYearAndMonth(Account account, Integer year, Integer month);
}
