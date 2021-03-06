package com.yapp.project.retrospect.domain;

import com.yapp.project.account.domain.Account;
import com.yapp.project.routine.domain.Routine;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {
    Optional<Retrospect> findByRoutineAndDate(Routine routine, LocalDate date);

    @EntityGraph(attributePaths = {"routine", "routine.days"})
    @Query("select m from Retrospect m where m.date= :date and m.routine.account= :account")
    List<Retrospect> findAllByDateAndRoutineAccount(LocalDate date, Account account);

    List<Retrospect> findAllByIsReportIsFalseAndRoutineAccount(Account account);

    List<Retrospect> findAllByDateBetweenAndRoutine(LocalDate start, LocalDate end, Routine routine);
    @EntityGraph(attributePaths = {"routine"})
    List<Retrospect> findAllByDateBetweenAndRoutineAccount(LocalDate start, LocalDate end, Account account);
}
