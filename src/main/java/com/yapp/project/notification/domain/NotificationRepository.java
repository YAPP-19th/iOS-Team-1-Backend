package com.yapp.project.notification.domain;

import com.yapp.project.account.domain.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @EntityGraph(attributePaths = "account")
    List<Notification> findAllByAccountAndDateIsAfterOrderByDateDesc(Account account, LocalDate date);
}
