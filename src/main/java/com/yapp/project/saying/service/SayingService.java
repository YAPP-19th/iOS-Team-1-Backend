package com.yapp.project.saying.service;

import static com.yapp.project.aux.content.SayingContent.*;
import static com.yapp.project.saying.domain.dto.SayingDto.*;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.saying.AlreadyFoundException;
import com.yapp.project.config.exception.saying.OverFlowSayingIdException;
import com.yapp.project.saying.domain.Saying;
import com.yapp.project.saying.domain.SayingRecord;
import com.yapp.project.saying.domain.repository.SayingRecordRepository;
import com.yapp.project.saying.domain.repository.SayingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SayingService {
    private final SayingRepository sayingRepository;
    private final SayingRecordRepository sayingRecordRepository;

    @Transactional(readOnly = true)
    public SayingAccessMessage randomSaying(Account account, int id){
        Saying saying =  sayingRepository.findById((long) id)
                .orElseThrow(OverFlowSayingIdException::new);

        SayingRecord lastRecord = sayingRecordRepository.findTopByAccount_IdOrderByIdDesc(account.getId())
                .orElse(null);

        if (lastRecord == null || isNotTodayRecordingSaying(lastRecord)){
            return SayingAccessMessage.of(StatusEnum.SAYING_OK, FIND_SAYING_SUCCESS, saying);
        }else{
            throw new AlreadyFoundException();
        }
    }

    @Transactional
    public SayingResponseMessage checkResult(SayingAccess request, Account account){
        Saying saying = sayingRepository.findById(request.getId())
                .orElseThrow(OverFlowSayingIdException::new);
        if (saying.getContent().equalsIgnoreCase(request.getContent())){
            SayingRecord sayingRecord = SayingRecord.builder().account(account).saying(saying).build();
            sayingRecordRepository.save(sayingRecord);
            SayingResponse data = SayingResponse.builder().id(request.getId()).result(true).build();
            return SayingResponseMessage.of(StatusEnum.SAYING_OK, WRITE_SAYING_SUCCESS,data);
        }else{
            SayingResponse data = SayingResponse.builder().id(request.getId()).result(false).build();
            return SayingResponseMessage.of(StatusEnum.SAYING_OK, WRITE_SAYING_FAIL,data);
        }
    }

    @Transactional(readOnly = true)
    public SayingRecordResponseMessage isTodayRecording(Account account){
        Long accountId = account.getId();
        SayingRecord sayingRecord = sayingRecordRepository.findTopByAccount_IdOrderByIdDesc(accountId).orElse(null);
        if (sayingRecord == null || isNotTodayRecordingSaying(sayingRecord)){
            SayingRecordResponse data = SayingRecordResponse.builder().result(false).build();
            return SayingRecordResponseMessage.of(StatusEnum.SAYING_OK, TODAY_SAYING_NOT_COMPLETE, data);
        }else{
            SayingRecordResponse data = SayingRecordResponse.builder().result(true).build();
            return SayingRecordResponseMessage.of(StatusEnum.SAYING_OK, TODAY_SAYING_COMPLETE, data);
        }
    }

    private boolean isNotTodayRecordingSaying(SayingRecord sayingRecord){
        return sayingRecord.getCreatedAt().toLocalDate().isBefore(LocalDateTime.now().toLocalDate());
    }
}
