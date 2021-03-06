package com.yapp.project.report.domain;

import com.yapp.project.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeekReportRepository extends JpaRepository<WeekReport, Long> {
    Boolean existsByAccountAndLastDate(Account account, LocalDate minusDays);

    List<WeekReport> findAllByAccount(Account account);

    List<WeekReport> findAllByAccountAndIsReportIsFalseOrderByLastDate(Account account);

    Optional<WeekReport> findByAccountAndLastDate(Account account, LocalDate lastDate);

    List<WeekReport> findAllByAccountAndMonthReportYearAndMonthReportMonthOrderByLastDate(Account account, Integer monthReportYear, Integer monthReportMonth);
}
