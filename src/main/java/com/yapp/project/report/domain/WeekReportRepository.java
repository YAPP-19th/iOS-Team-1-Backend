package com.yapp.project.report.domain;

import com.yapp.project.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeekReportRepository extends JpaRepository<WeekReport, Long> {

    List<WeekReport> findAllByAccount(Account account);

    Optional<WeekReport> findByAccountAndLastDate(Account account, LocalDate lastDate);

    Boolean existsByAccountAndLastDate(Account account, LocalDate minusDays);

    List<WeekReport> findAllByAccountAndIsReportIsFalseOrderByLastDate(Account account);

    List<WeekReport> findAllByAccountAndMonthReportYearAndMonthReportMonthOrderByLastDate(Account account, Integer monthReportYear, Integer monthReportMonth);
}
