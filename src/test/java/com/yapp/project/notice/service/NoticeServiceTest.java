package com.yapp.project.notice.service;

import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.config.exception.notice.NoticeNotFoundException;
import com.yapp.project.notice.domain.Notice;
import com.yapp.project.notice.domain.repository.NoticeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.yapp.project.notice.domain.dto.NoticeDto.NoticeDetailResponseMessage;
import static com.yapp.project.notice.domain.dto.NoticeDto.NoticeListResponseMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class NoticeServiceTest {
    @Mock
    private NoticeRepository noticeRepository;

    @InjectMocks
    private NoticeService noticeService;

    @Test
    void test_전체_공지사항_조회() {
        //given
        Notice notice = new Notice(1L,"미닝 공지", "미닝이 출시되었습니다.", DateUtil.KST_LOCAL_DATE_NOW());
        Notice notice2 = new Notice(2L,"일시 중지", "당분간 중지...", DateUtil.KST_LOCAL_DATE_NOW());
        List<Notice> notices = new ArrayList<>(Arrays.asList(notice, notice2));
        given(noticeRepository.findAll()).willReturn(notices);
        //when
        NoticeListResponseMessage response = noticeService.findAll();
        //then
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().size()).isEqualTo(2);
    }

    @Test
    void test_단일_조회() {
        //given
        Notice notice = new Notice(1L,"미닝 공지", "미닝이 출시되었습니다.", DateUtil.KST_LOCAL_DATE_NOW());
        given(noticeRepository.findById(1L)).willReturn(Optional.of(notice));
        //when
        NoticeDetailResponseMessage response = noticeService.findDetail(1L);
        //then
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getContent()).isEqualTo("미닝이 출시되었습니다.");
    }

    @Test
    void test_단일_조회_없을_때() {
        //given
        Notice notice = new Notice(1L,"미닝 공지", "미닝이 출시되었습니다.", DateUtil.KST_LOCAL_DATE_NOW());
        given(noticeRepository.findById(2L)).willReturn(Optional.empty());
        //when  -> then
        assertThatThrownBy(() ->noticeService.findDetail(2L)).isInstanceOf(NoticeNotFoundException.class)
                .hasMessage("해당 공지사항은 보이지 않습니다.");
    }

}