package com.yapp.project.saying.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.saying.SayingTemplate;
import com.yapp.project.config.exception.Content;
import com.yapp.project.config.exception.saying.AlreadyFoundException;
import com.yapp.project.saying.domain.Saying;
import com.yapp.project.saying.domain.dto.SayingDto;
import com.yapp.project.saying.domain.repository.SayingRecordRepository;
import com.yapp.project.saying.domain.repository.SayingRepository;
import com.yapp.project.saying.utils.SayingUtils;
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
        int id = SayingUtils.randomSayingId();
        given(sayingRepository.findById((long)id)).willReturn(Optional.of(SayingTemplate.makeSaying()));
        Saying saying = sayingService.randomSaying(account, id);
        System.out.println(saying);
        assertThat(saying.getContent()).isEqualTo(SayingTemplate.CONTENT);
    }

    @Test
    void test_명언쓰기를_완성_했을_때(){
        Account account = AccountTemplate.makeTestAccount();
        int id = SayingUtils.randomSayingId();
        given(sayingRepository.findById((long)id)).willReturn(Optional.of(SayingTemplate.makeSaying()));
        given(sayingRecordRepository.findTopByAccount_IdOrderByIdDesc(account.getId()))
                .willReturn(Optional.of(SayingTemplate.makeSayingRecord()));
        assertThatThrownBy(() -> sayingService.randomSaying(account, id)).isInstanceOf(AlreadyFoundException.class)
                .hasMessage(Content.ALREADY_FOUND_SAYING_RECORD);
    }

    @Test
    void test_명언쓰기_성공_했을_때(){
        Account account = AccountTemplate.makeTestAccount();
        SayingDto.SayingAccess request = SayingTemplate.makeSayingAccess();
        Saying saying = SayingTemplate.makeSaying();
        given(sayingRepository.findById(request.getId())).willReturn(Optional.of(saying));
        SayingDto.SayingResponse response = sayingService.checkResult(request, account);
        assertThat(response.getId()).isEqualTo(request.getId());
        assertThat(response.getResult()).isTrue();
    }

    @Test
    void test_명언쓰기_실패_했을_때(){
        Account account = AccountTemplate.makeTestAccount();
        SayingDto.SayingAccess request = SayingTemplate.makeSayingAccess("");
        Saying saying = SayingTemplate.makeSaying();
        given(sayingRepository.findById(request.getId())).willReturn(Optional.of(saying));
        SayingDto.SayingResponse response = sayingService.checkResult(request, account);
        assertThat(response.getId()).isEqualTo(request.getId());
        assertThat(response.getResult()).isFalse();
    }

}