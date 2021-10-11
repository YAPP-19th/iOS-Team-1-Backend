package com.yapp.project.organization.domain.repository;

import com.yapp.project.organization.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization,Long> {
    @Query(value = "SELECT * FROM Organization WHERE category=?1",nativeQuery = true)
    List<Organization> findByCategoryAndMore(String category);
}
