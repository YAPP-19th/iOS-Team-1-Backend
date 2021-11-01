package com.yapp.project.report.controller;

import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.report.domain.dto.ReportDTO;
import com.yapp.project.report.service.ReportService;
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

    /** Todo change Schedule - Batch*/
    @GetMapping("/week/test")
    public void testWeekMethod() {
       reportService.makeWeekReport(AccountUtil.getAccount());
    }

    /** Todo change Schedule - Batch*/
    @GetMapping("/month/test")
    public void asdgetMonthReportByYearAndMonth() {
        reportService.makeMonthReport(AccountUtil.getAccount());
    }

    @GetMapping("/month/{year}/{month}")
    public ReportDTO.ResponseMonthReportMessage getMonthReportByYearAndMonth(
            @PathVariable Integer year, @PathVariable Integer month) {
        return reportService.getMonthReportByYearAndMonth(AccountUtil.getAccount(), year, month);
    }

    @GetMapping("/week/{lastDate}")
    public ReportDTO.ResponseWeekReportMessage getWeekReport(@PathVariable String lastDate) {
        return reportService.getWeekReportLastDate(AccountUtil.getAccount(), LocalDate.parse(lastDate));
    }


}
