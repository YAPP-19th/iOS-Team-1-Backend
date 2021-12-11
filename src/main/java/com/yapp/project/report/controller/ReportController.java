package com.yapp.project.report.controller;

import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.report.domain.dto.ReportDTO;
import com.yapp.project.report.service.ReportService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/")
public class ReportController {

    private final ReportService reportService;

    @ApiOperation(value = "월 리포트 조회", notes = "월 리포트 조회하기. \n 월 리포트의 년도와 월을 넘겨주세요. \n ex) 2021/10" +
            "\n 응답 예시에 additionalProp1, ..2는 카테고리 명입니다. additionalProp1대신에 아래 키로 받으시면 됩니다." +
            "\n [miracle, self, health, daily, etc]")
    @GetMapping("/month/{year}/{month}")
    public ReportDTO.ResponseMonthReportMessage getMonthReportByYearAndMonth(
            @PathVariable Integer year, @PathVariable Integer month) {
        return reportService.getMonthReportByYearAndMonth(AccountUtil.getAccount(), year, month);
    }

    @ApiOperation(value = "주 리포트 조회", notes = "주 리포트 조회하기. \n 주 리포트의 마지막 날을 넘겨주세요. \n ex) 2021-10-24")
    @GetMapping("/week/{lastDate}")
    public ReportDTO.ResponseWeekReportMessage getWeekReport(@PathVariable String lastDate) {
        return reportService.getWeekReportLastDate(AccountUtil.getAccount(), LocalDate.parse(lastDate));
    }
}