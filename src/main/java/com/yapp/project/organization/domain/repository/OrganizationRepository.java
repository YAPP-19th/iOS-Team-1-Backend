package com.yapp.project.organization.domain.repository;

import com.yapp.project.organization.domain.Category;
import com.yapp.project.organization.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization,Long> {
    @Query("SELECT o FROM Organization o WHERE o.category=?1")
    List<Organization> findByCategoryAndMore(Category category);

    @Query("SELECT o FROM Organization o WHERE o.category=?1 AND o.id NOT IN ?2")
    List<Organization> findByCategoryAndMoreAndNotIn(String category, List<Long> organizations);

    @Query("SELECT o FROM Organization o WHERE o.id NOT IN ?1")
    List<Organization> findOrganizationsNotIn(List<Long> organizations);
}
