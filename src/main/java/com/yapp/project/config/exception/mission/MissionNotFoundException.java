package com.yapp.project.config.exception.mission;

import com.yapp.project.aux.StatusEnum;
import lombok.Getter;

@Getter
public class MissionNotFoundException extends IllegalArgumentException{
    private final StatusEnum status;
    public MissionNotFoundException(){
        super(MissionContent.MISSION_NOT_FOUND);
        this.status = StatusEnum.MISSION_NOT_FOUND;
    }
}
