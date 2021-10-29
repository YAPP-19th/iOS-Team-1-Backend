package com.yapp.project.account.service;


import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.SocialType;
import com.yapp.project.account.domain.dto.AccountDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.account.util.PasswordUtil;
import com.yapp.project.account.util.SecurityUtil;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import com.yapp.project.capture.domain.Capture;
import com.yapp.project.capture.domain.CaptureImage;
import com.yapp.project.capture.domain.repository.CaptureImageRepository;
import com.yapp.project.config.exception.account.NotFoundUserInformationException;
import com.yapp.project.config.exception.account.PasswordInvalidException;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.repository.MissionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final MissionRepository missionRepository;
    private final CaptureImageRepository captureImageRepository;

    @Transactional(readOnly = true)
    public AccountDto.UserResponse getUserInfo() {
        return accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                .map(AccountDto.UserResponse::of)
                .orElseThrow(NotFoundUserInformationException::new);
    }

    @Transactional
    public Message resetMyAccountPassword(AccountDto.ProfilePasswordRequest request, Account account){
        String password = request.getPassword();
        if (!account.getSocialType().equals(SocialType.NORMAL)){
            throw new AssertionError();
        }
        Account dbAccount = accountRepository.findByEmail(account.getEmail()).orElseThrow(NotFoundUserInformationException::new);
        if(!PasswordUtil.validPassword(password)){
            throw new PasswordInvalidException();
        }
        dbAccount.resetPassword(passwordEncoder,password);
        return Message.of(StatusEnum.ACCOUNT_OK, AccountContent.ACCOUNT_OK_MSG);
    }

    @Transactional
    public Message removeAccount(Account account) throws NoSuchAlgorithmException {
        List<Mission> list = missionRepository.findAllByAccountAndIsFinishIsFalseAndIsDeleteIsFalse(account);
        deleteMyMission(list);
        //루틴, 리포트 관련 isDelete 세팅 필요
        account.remove();
        return Message.of(StatusEnum.ACCOUNT_OK, AccountContent.ACCOUNT_OK_MSG);
    }
    
    private void deleteMyMission(List<Mission> list){
        for (Mission mission : list){
            List<Capture> basket = mission.getCaptures();
            for (Capture value : basket){
                List<CaptureImage> images = value.getCaptureImage();
                captureImageRepository.deleteAllInBatch(images);
                value.remove();
            }
            mission.remove();
        }
    }
}
