package com.yapp.project.routine.domain.dto;

import com.yapp.project.routine.domain.Week;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RequestRoutineDto {
    @ApiModelProperty(value = "타이틀", example = "단어 외우기", required = true)
    private String title;

    @ApiModelProperty(value = "목표", example = "티끌 모아 태산! 오늘부터 시작해보는건 어때요?", required = true)
    private String goal;

    @ApiModelProperty(value = "하는 요일", example = "['MON', 'SUN']", required = true)
    private List<Week> days = new ArrayList<>();

    @ApiModelProperty(value = "하는 시간", example = "07:35", required = true)
    private String startTime;

    @ApiModelProperty(value = "카테고리", example = "생활", required = true)
    private String category;
}
