package com.yapp.project.notice.service;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.NoticeContent;
import com.yapp.project.config.exception.notice.NoticeNotFoundException;
import com.yapp.project.notice.domain.Notice;
import com.yapp.project.notice.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yapp.project.notice.domain.dto.NoticeDto.NoticeDetailResponseMessage;
import static com.yapp.project.notice.domain.dto.NoticeDto.NoticeListResponseMessage;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public NoticeListResponseMessage findAll() {
        List<Notice> notices = noticeRepository.findAll();
        return NoticeListResponseMessage.of(StatusEnum.NOTICE_OK, NoticeContent.NOTICE_FIND_ALL_OK, notices);
    }

    @Transactional(readOnly = true)
    public NoticeDetailResponseMessage findDetail(Long id) {
        Notice notice = noticeRepository.findById(id).orElseThrow(NoticeNotFoundException::new);
        return NoticeDetailResponseMessage.of(StatusEnum.NOTICE_OK, NoticeContent.NOTICE_FIND_OK, notice);
    }

}
