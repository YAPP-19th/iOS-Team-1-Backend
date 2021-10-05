package com.yapp.project.account.service;


import com.yapp.project.account.domain.dto.AccountResponseDto;
import com.yapp.project.account.domain.dto.NicknameRequestDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.account.util.SecurityUtil;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.Content;
import com.yapp.project.config.exception.account.DuplicateException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public AccountResponseDto getUserInfo() {
        return accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                .map(AccountResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    @Transactional(readOnly = true)
    public Message existByNickname(NicknameRequestDto requestDto){
        if (accountRepository.existsByNickname(requestDto.getNickname()))
            throw new DuplicateException(Content.NICKNAME_DUPLICATE, StatusEnum.BAD_REQUEST);
        return Message.of("중복되는 닉네임이 없습니다.");
    }

}
