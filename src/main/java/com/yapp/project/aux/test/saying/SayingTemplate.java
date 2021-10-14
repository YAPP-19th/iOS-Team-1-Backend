package com.yapp.project.aux.test.saying;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.saying.domain.Saying;
import com.yapp.project.saying.domain.SayingRecord;
import com.yapp.project.saying.domain.dto.SayingDto;

public class SayingTemplate {
    private SayingTemplate(){
    }
    private static final Long ID = 100000000000L;
    public static final String AUTHOR = "마커스 아우렐리우스";
    public static final String CONTENT = "아침에 일어나면, 살아 있음과 숨을 쉬는 것,생각하는 것,즐기는 것,그리고 사랑하는 것이 얼마나 " +
            "귀중한 특권인지 생각해보라";

    public static Saying makeSaying(){
        return Saying.builder().id(ID).author(AUTHOR).content(CONTENT).build();
    }
    public static SayingRecord makeSayingRecord(){
        return makeSayingRecord(AccountTemplate.makeTestAccount(),makeSaying());
    }
    public static SayingRecord makeSayingRecord(Account account,Saying saying){
        return SayingRecord.builder().saying(saying).account(account).build();
    }
    public static SayingDto.SayingAccess makeSayingAccess(){
        return SayingDto.SayingAccess.builder().id(ID).content(CONTENT).build();
    }
    public static SayingDto.SayingAccess makeSayingAccess(String content){
        return SayingDto.SayingAccess.builder().id(ID).content(content).build();
    }

}
