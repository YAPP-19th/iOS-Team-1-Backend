package com.yapp.project.account.service;


import com.yapp.project.account.domain.dto.AccountDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.account.util.SecurityUtil;
import com.yapp.project.config.exception.account.NotFoundUserInformationException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public AccountDto.UserResponse getUserInfo() {
        return accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                .map(AccountDto.UserResponse::of)
                .orElseThrow(NotFoundUserInformationException::new);
    }
}
