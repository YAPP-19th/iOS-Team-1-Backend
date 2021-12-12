package com.yapp.project.notice.controller;

import com.yapp.project.notice.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "공지사항")
public class NoticeController {
    private final NoticeService noticeService;

    @ApiOperation(value = "공지리스트")
    @GetMapping
    public NoticeListResponseMessage findAll() {
        return noticeService.findAll();
    }

    @ApiOperation(value = "공지 디테일 페이지")
    @GetMapping("/{id}")
    public NoticeDetailResponseMessage findDetail(@PathVariable Long id) {
        return noticeService.findDetail(id);
    }

}
