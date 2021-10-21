package com.yapp.project.snapshot.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {
    Optional<Snapshot> findByUrl(String url);
}
