package com.yapp.project.report.domain;

import com.yapp.project.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WeekReportRepository extends JpaRepository<WeekReport, Long> {
    Boolean existsByAccountAndLastDate(Account account, LocalDate minusDays);

    List<WeekReport> findAllByAccountAndIsReportIsFalseOrderByLastDate(Account account);
}
