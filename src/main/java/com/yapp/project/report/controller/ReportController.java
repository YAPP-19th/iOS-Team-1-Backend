package com.yapp.project.report.controller;

import com.yapp.project.aux.common.AccountUtil;
import com.yapp.project.report.domain.MonthRoutineReport;
import com.yapp.project.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /** Todo change Schedule - Batch
     * @return*/
    @GetMapping("/month/test")
    public void testMonthMethod() {
        reportService.makeMonthReport(AccountUtil.getAccount());
    }
}
