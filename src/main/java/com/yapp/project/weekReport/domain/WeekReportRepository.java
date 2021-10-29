package com.yapp.project.weekReport.domain;

import com.yapp.project.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WeekReportRepository extends JpaRepository<WeekReport, Long> {
    Boolean existsByAccountAndLastDate(Account account, LocalDate minusDays);
}
