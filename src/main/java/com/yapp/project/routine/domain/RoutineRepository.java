package com.yapp.project.routine.domain;

import com.yapp.project.account.domain.Account;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findAllByAccountAndDaysDayOrderByDaysSequence(Account account, Week days, Sort sort);

    Optional<Routine> findByIdAndIsDelete(Long routineId, Boolean isDelete);
}