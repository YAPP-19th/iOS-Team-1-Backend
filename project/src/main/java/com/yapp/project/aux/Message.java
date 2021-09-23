package com.yapp.project.aux;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private StatusEnum status;
    private String msg;
    private Object data;

    public static Message of(StatusEnum status, String message){
        return Message.builder().status(status)
                                .msg(message)
                                .build();
    }

}
