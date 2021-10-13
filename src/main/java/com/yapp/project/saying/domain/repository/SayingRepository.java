package com.yapp.project.saying.domain.repository;

import com.yapp.project.saying.domain.Saying;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SayingRepository extends JpaRepository<Saying,Long> {
}
