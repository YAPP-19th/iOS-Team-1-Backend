package com.yapp.project.routine.domain;

import com.yapp.project.account.domain.Account;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findAllByIsDeleteIsFalseAndAccountAndDaysDayOrderByDaysSequence(Account account, Week days, Sort sort);

    Optional<Routine> findByIdAndIsDeleteIsFalse(Long routineId);

    List<Routine> findAllByAccountAndDaysDayAndRetrospectsDate(Account account, Week day, LocalDate parse);

    List<Routine> findAllByIsDeleteIsFalseAndAccount(Account account);

    List<Routine> findAllByIsDeleteIsFalseAndAccountAndRetrospectsDateBetween(Account account, LocalDate start, LocalDate end);
}