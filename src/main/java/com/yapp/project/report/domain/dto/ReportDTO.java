package com.yapp.project.report.domain.dto;

import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.report.domain.*;
import com.yapp.project.retrospect.domain.Result;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.yapp.project.aux.content.ReportContent.MONTH_REPORT_OK;
import static com.yapp.project.aux.content.ReportContent.WEEK_REPORT_OK;

public class ReportDTO {

    private static final int categoryCnt = 6;
    private static final HashMap<String, String> category = new HashMap<>() {
        { put("미라클모닝", "miracle"); put("자기개발", "self"); put("건강", "health");
            put("생활", "daily"); put("기타", "etc");} };
    private static final HashMap<String, List<ResponseMonthRoutineReport>> routineResult = new HashMap<>(categoryCnt);
    private static final HashMap<String, ResponseMonthByCategory> resultByCategory = new HashMap<>(categoryCnt);

    @Getter
    public static class ResponseMonthByCategory {
        @ApiModelProperty(value = "카테고리 비율", example = "30")
        private final int rate;
        private final List<ResponseMonthRoutineReport> routineReportList;
        @Builder
        public ResponseMonthByCategory(int rate, List<ResponseMonthRoutineReport> routineReportList) {
            this.rate = rate;
            this.routineReportList = routineReportList;
        }
    }

    @Getter
    public static class ResponseMonthRoutineReport {
        @ApiModelProperty(value = "루틴이름", example = "물 1잔 꼭 챙겨마시기!")
        private final String title;
        @ApiModelProperty(value = "카테고리", example = "생활")
        private final String category;
        @ApiModelProperty(value = "완료 수행률", example = "50%")
        private final String fullyDoneRate;
        @ApiModelProperty(value = "부분완료 수행률", example = "0%")
        private final String partiallyDoneRate;
        @ApiModelProperty(value = "미완료 수행률", example = "50%")
        private final String notDoneRate;

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
    public static class ResponseWeekRoutineReport {
        @ApiModelProperty(value = "수행률", example = "30%")
        private String rate;
        @ApiModelProperty(value = "주 마지막 날(일)", example = "2021-10-24")
        private LocalDate lastDate;
        @ApiModelProperty(value = "완료수", example = "5")
        private int fullyDoneCount;
        @ApiModelProperty(value = "부분완료수", example = "2")
        private int partiallyDoneCount;
        @ApiModelProperty(value = "실패수", example = "13")
        private int notDoneCount;
        private List<ResponseWeekRoutineList> responseWeekRoutine;

        @Builder
        public ResponseWeekRoutineReport(WeekReport report) {
            this.rate = report.getRate();
            this.lastDate = report.getLastDate();
            this.fullyDoneCount = report.getFullyDoneCount();
            this.partiallyDoneCount = report.getPartiallyDoneCount();
            this.notDoneCount = report.getNotDoneCount();
            this.responseWeekRoutine =
                    report.getRoutineResults().stream().map( routineResult ->
                            ResponseWeekRoutineList.builder().routineResult(routineResult).build()
                    ).collect(Collectors.toList());

        }
    }

    @Getter
    public static class ResponseWeekRoutineList {
        @ApiModelProperty(value = "루틴이름", example = "1챕터 꼭꼭! 읽기")
        private String title;
        private List<ResponseWeekRetrospectReport> retrospectDayList;

        @Builder
        public ResponseWeekRoutineList(RoutineResult routineResult) {
            this.title = routineResult.getTitle();
            this.retrospectDayList = routineResult.getRetrospectReportDays().stream().map( retrospectReportDay ->
                    ResponseWeekRetrospectReport.builder().retrospectReportDay(retrospectReportDay).build()).collect(Collectors.toList());
        }
    }

    @Getter
    public static class ResponseWeekRetrospectReport {
        @ApiModelProperty(value = "수행 결과", example = "DONE")
        private Result result;
        @ApiModelProperty(value = "요일", example = "THU")
        private String day;

        @Builder
        public ResponseWeekRetrospectReport(RetrospectReportDay retrospectReportDay) {
            this.result = retrospectReportDay.getResult();
            this.day = retrospectReportDay.getDay();
        }
    }

    @Getter
    public static class ResponseMonthReport {
        @ApiModelProperty(value = "주차별 수행률", example = "['30%', '40%', '67%', '48%']")
        List<String> weekRateList;
        HashMap<String, ResponseMonthByCategory> resultByCategory;

        @Builder
        public ResponseMonthReport(List<String> weekRateList, HashMap<String, ResponseMonthByCategory> resultByCategory) {
            this.weekRateList = weekRateList;
            this.resultByCategory = resultByCategory;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ResponseMonthReportMessage {
        private Message message;
        private ResponseMonthReport data;

        public static ResponseMonthReportMessage of(List<MonthRoutineReport> monthReportList,
                                                    List<String> weekRateList) {
            List<ResponseMonthRoutineReport> routineReportList = monthReportList.stream().map(monthRoutineReport ->
                    ResponseMonthRoutineReport.builder().report(monthRoutineReport).build()
            ).collect(Collectors.toList());
            HashMap<String, ResponseMonthByCategory> listByCategory = createResponseByCategoryList(routineReportList);
            ResponseMonthReport data = ResponseMonthReport.builder()
                    .weekRateList(weekRateList).resultByCategory(listByCategory).build();
            return ResponseMonthReportMessage.builder().message(
                    Message.builder().status(StatusEnum.MONTH_REPORT_OK).msg(MONTH_REPORT_OK).build()
            ).data(data).build();
        }

        private static HashMap<String, ResponseMonthByCategory> createResponseByCategoryList(
                List<ResponseMonthRoutineReport> routineReportList) {
            initialize(routineResult);
            routineReportList.forEach(x -> routineResult.get(x.getCategory()).add(x));
            routineResult.forEach((key, value)-> {
                int rate = (int) (((double)value.size() / routineReportList.size()) * 100);
                ResponseMonthByCategory responseByCategory = ResponseMonthByCategory.builder()
                        .rate(rate).routineReportList(value).build();
                resultByCategory.put(category.get(key), responseByCategory);
            });
            return resultByCategory;
        }

        private static void initialize(HashMap<String, List<ResponseMonthRoutineReport>> routineResult) {
            category.forEach((key, value) ->
                    routineResult.put(key, new ArrayList<>()));
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ResponseWeekReportMessage {
        private Message message;
        private ResponseWeekRoutineReport data;
        public static ResponseWeekReportMessage of(WeekReport weekReport) {

            return ResponseWeekReportMessage.builder().message(
                    Message.builder().status(StatusEnum.WEEK_REPORT_OK).msg(WEEK_REPORT_OK).build()
            ).data(ReportDTO.ResponseWeekRoutineReport.builder().report(weekReport).build()).build();
        }
    }
}