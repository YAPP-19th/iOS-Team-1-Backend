package com.yapp.project.routine.domain;

import com.yapp.project.account.domain.Account;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    List<Routine> findAllByIsDeleteIsFalseAndAccountAndDaysDayOrderByDaysSequence(Account account, Week days, Sort sort);

    @EntityGraph(attributePaths = {"days"})
    @Query("select m from Routine m where m.id= :routineId")
    Optional<Routine> findByIdAndIsDeleteIsFalse(Long routineId);

    @EntityGraph(attributePaths = {"days"})
    List<Routine> findAllByIsDeleteIsFalseAndAccount(Account account);
}