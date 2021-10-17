package com.yapp.project.organization.domain.repository;

import static com.yapp.project.aux.test.organization.OrganizationTemplate.*;
import com.yapp.project.organization.domain.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrganizationRepositoryTest {

    @Autowired
    OrganizationRepository organizationRepository;

    @Test
    void test_카테고리로_그룹_조회(){
        Organization organization = makeTestOrganization();
        organizationRepository.save(organization);
        List<Organization> organizations = organizationRepository.findByCategoryAndMore(CATEGORY);
        assertThat(organizations.get(0).getCategory()).isEqualTo(CATEGORY);
    }
}