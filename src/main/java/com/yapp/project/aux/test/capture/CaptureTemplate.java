package com.yapp.project.aux.test.capture;

import com.yapp.project.capture.domain.Achievement;
import com.yapp.project.capture.domain.Capture;
import com.yapp.project.capture.domain.CaptureImage;
import com.yapp.project.mission.domain.Mission;

public class CaptureTemplate {
    private CaptureTemplate() {
    }
    public static final String URL = "/home/image/model";
    public static Capture makeCapture(Mission mission, String url){
        Integer myAchievementRate = mission.getAchievementRate();
        Integer groupAchievementRate = mission.getOrganization().getRate();
        Integer rank = mission.getOrganization().getCount()+1;
        Achievement achievement = Achievement.builder().myAchievementRate(myAchievementRate)
                .groupAchievementRate(groupAchievementRate).build();
        Capture capture = Capture.builder().achievement(achievement).mission(mission).organization(mission.getOrganization())
                .rank(rank).build();
        capture.updateCaptureImage(makeCaptureImage(capture,url));
        return  capture;
    }

    public static CaptureImage makeCaptureImage(Capture capture, String url){
        return CaptureImage.builder().capture(capture).url(url).build();
    }

    public static CaptureImage makeCaptureImage(Capture capture){
        return CaptureImage.builder().capture(capture).url(URL).build();
    }

    public static Capture makeCapture(Mission mission){
        return makeCapture(mission, URL);
    }

}
