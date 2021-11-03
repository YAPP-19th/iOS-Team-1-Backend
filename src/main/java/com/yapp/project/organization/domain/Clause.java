package com.yapp.project.organization.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Clause {
    private String shoot;
    private Integer beginTime;
    private Integer endTime;
}
