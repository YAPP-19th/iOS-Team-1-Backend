package com.yapp.project.saying.domain.repository;

import com.yapp.project.account.domain.Account;
import com.yapp.project.saying.domain.SayingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface SayingRecordRepository extends JpaRepository<SayingRecord, Long> {
    Optional<SayingRecord> findByAccountAndCreatedAt(Account account, LocalDateTime date);
    Optional<SayingRecord> findTopByAccount_IdOrderByIdDesc(Long id);
}
