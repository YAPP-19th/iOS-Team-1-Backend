package com.yapp.project.organization.domain.repository;

import com.yapp.project.organization.domain.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class OrganizationRepositoryTest {

    @Autowired
    OrganizationRepository organizationRepository;

    @Test
    @Transactional
    void test_카테고리로_그룹_조회(){
        List<Organization> organizations = organizationRepository.findByCategoryAndMore("건강");
        assertThat(organizations.size()).isEqualTo(5);
        assertThat(organizations.get(0).getCategory()).isEqualTo("건강");
        assertThat(organizations.get(0).getTitle()).isIn("요가","산책하기","스트레칭","러닝");
    }
}