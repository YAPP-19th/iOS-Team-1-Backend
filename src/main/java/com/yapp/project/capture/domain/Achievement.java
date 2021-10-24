package com.yapp.project.capture.domain;

import com.yapp.project.mission.domain.Mission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Achievement {
    private Integer myAchievementRate;

    private Integer groupAchievementRate;

    public static Achievement of(Mission mission){
        Integer myAchievementRate = mission.getAchievementRate();
        Integer groupAchievementRate = mission.getOrganization().getRate();
        return Achievement.builder().myAchievementRate(myAchievementRate).groupAchievementRate(groupAchievementRate)
                .build();
    }
}
