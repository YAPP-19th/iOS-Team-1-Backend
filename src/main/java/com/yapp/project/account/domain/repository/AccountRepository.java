package com.yapp.project.account.domain.repository;

import com.yapp.project.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    List<Account> findAllByIsAlarmIsTrueAndIsDeleteIsFalse();
}
