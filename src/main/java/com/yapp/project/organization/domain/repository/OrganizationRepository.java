package com.yapp.project.organization.domain.repository;

import com.yapp.project.organization.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization,Long> {

    @Query(value = "SELECT title,image,achievementRate FROM Organization WHERE theme=?1  ORDER BY id DESC OFFSET ?2 LIMIT ?3",nativeQuery = true)
    List<Organization> findByThemeAndMore(String theme,int limit, int offset);
}
