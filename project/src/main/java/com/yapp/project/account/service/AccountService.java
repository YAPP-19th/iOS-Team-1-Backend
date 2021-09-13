package com.yapp.project.account.service;


import com.yapp.project.account.domain.dto.AccountResponseDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.account.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public AccountResponseDto getAccountInfo(String email){
        return accountRepository.findByEmail(email)
                .map(AccountResponseDto::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    @Transactional(readOnly = true)
    public AccountResponseDto getUserInfo() {
        return accountRepository.findById(SecurityUtil.getCurrentAccountId())
                .map(AccountResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

    }



}
