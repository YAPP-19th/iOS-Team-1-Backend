package com.yapp.project.organization.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Clause {
    private String shoot;
    private LocalTime beginTime;
    private LocalTime endTime;
}
