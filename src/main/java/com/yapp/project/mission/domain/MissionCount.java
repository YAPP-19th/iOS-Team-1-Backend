package com.yapp.project.mission.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MissionCount {
    private Integer successCount;
    private Integer failureCount;
}
