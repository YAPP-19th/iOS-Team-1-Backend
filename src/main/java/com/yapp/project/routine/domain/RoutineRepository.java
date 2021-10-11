package com.yapp.project.routine.domain;

import com.yapp.project.account.domain.Account;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findAllByAccountAndDaysDayOrderByDaysSequence(Account account, Week days, Sort sort);
}