package com.yapp.project.notice.controller;

import com.yapp.project.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yapp.project.notice.domain.dto.NoticeDto.NoticeDetailResponseMessage;
import static com.yapp.project.notice.domain.dto.NoticeDto.NoticeListResponseMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    public NoticeListResponseMessage findAll() {
        return noticeService.findAll();
    }

    @GetMapping("/{id}")
    public NoticeDetailResponseMessage findDetail(@PathVariable Long id) {
        return noticeService.findDetail(id);
    }

}
