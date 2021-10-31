package com.yapp.project.report.domain.dto;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.report.domain.MonthRoutineReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yapp.project.aux.content.ReportContent.MONTH_REPORT_OK;

public class ReportDTO {

    @Getter
    public static class ResponseMonthRoutineReport {
        private String title;
        private String category;
        private String fullyDoneRate;
        private String partiallyDoneRate;
        private String notDoneRate;

        @Builder
        public ResponseMonthRoutineReport(MonthRoutineReport report) {
            double allCount = report.getFullyDoneCount() + report.getPartiallyDoneCount() + report.getNotDoneCount();
            this.title = report.getTitle();
            this.category = report.getCategory();

            this.fullyDoneRate = String.format("%.0f", ((double)report.getFullyDoneCount() / allCount) * 100) + '%';
            this.partiallyDoneRate = String.format("%.0f", ((double)report.getPartiallyDoneCount() / allCount) * 100) + '%';
            this.notDoneRate = String.format("%.0f", ((double)report.getNotDoneCount() / allCount) * 100) + '%';
        }
    }

    @Getter
    public static class ResponseMonthReport {
        List<String> weekRateList = new ArrayList<>();
        List<ResponseMonthRoutineReport> monthRoutineReportList = new ArrayList<>();

        @Builder
        public ResponseMonthReport(List<String> weekRateList, List<ResponseMonthRoutineReport> monthRoutineReportList) {
            this.weekRateList = weekRateList;
            this.monthRoutineReportList = monthRoutineReportList;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ResponseMonthReportMessage {
        private Message message;
        private ResponseMonthReport data;

        public static ResponseMonthReportMessage of(List<MonthRoutineReport> monthReportList, List<String> weekRateList) {
            List<ResponseMonthRoutineReport> routineReportList = monthReportList.stream().map(monthRoutineReport ->
                    ResponseMonthRoutineReport.builder().report(monthRoutineReport).build()
            ).collect(Collectors.toList());
            ResponseMonthReport responseMonthReport = ResponseMonthReport.builder().weekRateList(weekRateList)
                    .monthRoutineReportList(routineReportList).build();

            return ResponseMonthReportMessage.builder().message(
                    Message.builder().status(StatusEnum.MONTH_REPORT_OK).msg(MONTH_REPORT_OK).build()
            ).data(responseMonthReport).build();
        }
    }
}