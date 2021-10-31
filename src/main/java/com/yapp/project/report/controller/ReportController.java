package com.yapp.project.report.controller;

import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.report.domain.dto.ReportDTO;
import com.yapp.project.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/month/{year}/{month}")
    public void getMonthReportByYearAndMonth(@PathVariable Integer year, @PathVariable Integer month) {
        reportService.getMonthReportByYearAndMonth(AccountUtil.getAccount(), year, month);
    }

    @GetMapping("/month/test")
    public void asdgetMonthReportByYearAndMonth() {
        reportService.makeMonthReport(AccountUtil.getAccount());
    }
}
