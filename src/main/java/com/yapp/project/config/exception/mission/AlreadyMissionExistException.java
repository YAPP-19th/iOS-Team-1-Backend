package com.yapp.project.config.exception.mission;

import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.MissionContent;
import lombok.Getter;

@Getter
public class AlreadyMissionExistException extends IllegalArgumentException{
    private final StatusEnum status;
    public AlreadyMissionExistException(){
        super(MissionContent.MISSION_BAD_REQUEST);
        this.status = StatusEnum.MISSION_ALREADY_EXISTS;
    }
}
