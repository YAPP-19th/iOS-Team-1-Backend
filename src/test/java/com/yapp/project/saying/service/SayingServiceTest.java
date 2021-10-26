package com.yapp.project.saying.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.saying.SayingTemplate;
import com.yapp.project.config.exception.saying.AlreadyFoundException;
import com.yapp.project.aux.content.SayingContent;
import com.yapp.project.saying.domain.Saying;
import com.yapp.project.saying.domain.SayingRecord;
import com.yapp.project.saying.domain.dto.SayingDto;
import com.yapp.project.saying.domain.repository.SayingRecordRepository;
import com.yapp.project.saying.domain.repository.SayingRepository;
import com.yapp.project.aux.common.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class SayingServiceTest {

    @Mock
    private SayingRepository sayingRepository;

    @Mock
    private SayingRecordRepository sayingRecordRepository;


    @InjectMocks
    private SayingService sayingService;

    @Test
    void test_아직_명언쓰기를_하지_않았을_때(){
        Account account = AccountTemplate.makeTestAccount();
        int id = Utils.randomSayingId();
        given(sayingRepository.findById((long)id)).willReturn(Optional.of(SayingTemplate.makeSaying()));
        SayingDto.SayingAccessMessage saying = sayingService.randomSaying(account, id);
        assertThat(saying.getData().getContent()).isEqualTo(SayingTemplate.CONTENT);
    }

    @Test
    void test_명언쓰기를_완성_했을_때(){
        Account account = AccountTemplate.makeTestAccount();
        int id = Utils.randomSayingId();
        given(sayingRepository.findById((long)id)).willReturn(Optional.of(SayingTemplate.makeSaying()));
        given(sayingRecordRepository.findTopByAccount_IdOrderByIdDesc(account.getId()))
                .willReturn(Optional.of(SayingTemplate.makeSayingRecord()));
        assertThatThrownBy(() -> sayingService.randomSaying(account, id)).isInstanceOf(AlreadyFoundException.class)
                .hasMessage(SayingContent.ALREADY_FOUND_SAYING_RECORD);
    }

    @Test
    void test_명언쓰기_성공_했을_때(){
        Account account = AccountTemplate.makeTestAccount();
        SayingDto.SayingAccess request = SayingTemplate.makeSayingAccess();
        Saying saying = SayingTemplate.makeSaying();
        given(sayingRepository.findById(request.getId())).willReturn(Optional.of(saying));
        SayingDto.SayingResponseMessage response = sayingService.checkResult(request, account);
        assertThat(response.getData().getId()).isEqualTo(request.getId());
        assertThat(response.getData().getResult()).isTrue();
    }

    @Test
    void test_명언쓰기_실패_했을_때(){
        Account account = AccountTemplate.makeTestAccount();
        SayingDto.SayingAccess request = SayingTemplate.makeSayingAccess("");
        Saying saying = SayingTemplate.makeSaying();
        given(sayingRepository.findById(request.getId())).willReturn(Optional.of(saying));
        SayingDto.SayingResponseMessage response = sayingService.checkResult(request, account);
        assertThat(response.getData().getId()).isEqualTo(request.getId());
        assertThat(response.getData().getResult()).isFalse();
    }

    @Test
    void test_오늘_명언_쓰기_했을_때(){
        Account account = AccountTemplate.makeTestAccount();
        Saying saying = SayingTemplate.makeSaying();
        SayingRecord sayingRecord = SayingTemplate.makeSayingRecord(account, saying);
        given(sayingRecordRepository.findTopByAccount_IdOrderByIdDesc(account.getId())).willReturn(Optional.of(sayingRecord));
        SayingDto.SayingRecordResponseMessage message = sayingService.isTodayRecording(account);
        boolean isTodayRecording = message.getData().getResult();
        assertThat(isTodayRecording).isTrue();
    }

    @Test
    void test_오늘_명언_쓰기_안_했을_때(){
        Account account = AccountTemplate.makeTestAccount();
        given(sayingRecordRepository.findTopByAccount_IdOrderByIdDesc(account.getId())).willReturn(Optional.empty());
        SayingDto.SayingRecordResponseMessage message = sayingService.isTodayRecording(account);
        boolean isTodayRecording = message.getData().getResult();
        assertThat(isTodayRecording).isFalse();
    }

}