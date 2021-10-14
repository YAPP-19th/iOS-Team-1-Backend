package com.yapp.project.saying.domain.repository;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.saying.SayingTemplate;
import com.yapp.project.saying.domain.Saying;
import com.yapp.project.saying.domain.SayingRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SayingRecordRepositoryTest {

    @Autowired
    private SayingRecordRepository sayingRecordRepository;

    @Autowired
    private SayingRepository sayingRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void findTopByAccount_IdOrderByIdDesc() {
        Account account = accountRepository.save(AccountTemplate.makeTestAccount());
        Saying saying = sayingRepository.save(SayingTemplate.makeSaying());
        SayingRecord sayingRecord = SayingTemplate.makeSayingRecord(account, saying);

        sayingRecordRepository.save(sayingRecord);
        Optional<SayingRecord> result = sayingRecordRepository.findTopByAccount_IdOrderByIdDesc(account.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getAccount().getId()).isEqualTo(account.getId());
        assertThat(result.get().getSaying().getContent()).isEqualTo(saying.getContent());
    }
}