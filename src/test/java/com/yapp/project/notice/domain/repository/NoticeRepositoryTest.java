package com.yapp.project.notice.domain.repository;

import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.notice.domain.Notice;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void test_공지사항_prepersist() {
        try(MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)) {
            dateUtil.when(DateUtil::KST_LOCAL_DATETIME_NOW).thenReturn(LocalDateTime.of(2021,12,11,12,1));
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.of(2021,12,11));
            dateUtil.when(DateUtil::MID_NIGHT).thenReturn(LocalDateTime.of(2021,12,11,0,0)); // 토요일
            Notice notice = Notice.builder().content("1").title("2").build();
            Notice first = noticeRepository.save(notice);
            assertThat(first.getCreatedAt()).isEqualTo(LocalDate.of(2021,12,11));
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.of(2021,12,12));
            entityManager.clear();
            Notice second = noticeRepository.findById(first.getId()).orElse(null);
            assertThat(second).isNotNull();
            second.setContentForTest("3");
            noticeRepository.save(second);
            Notice third = noticeRepository.getById(second.getId());
            assertThat(third.getContent()).isEqualTo("3");
            assertThat(DateUtil.KST_LOCAL_DATE_NOW()).isEqualTo(LocalDate.of(2021,12,12));
            assertThat(third.getCreatedAt()).isEqualTo(LocalDate.of(2021,12,12));
        }
    }

}