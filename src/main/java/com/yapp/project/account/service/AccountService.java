package com.yapp.project.account.service;


import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.SocialType;
import com.yapp.project.account.domain.dto.AccountDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.account.util.RegexUtil;
import com.yapp.project.account.util.SecurityUtil;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import com.yapp.project.config.exception.account.NotFoundUserInformationException;
import com.yapp.project.config.exception.account.PasswordInvalidException;
import com.yapp.project.mission.domain.Mission;
import com.yapp.project.mission.domain.repository.MissionRepository;
import com.yapp.project.mission.service.MissionService;
import com.yapp.project.report.domain.MonthRoutineReport;
import com.yapp.project.report.domain.MonthRoutineReportRepository;
import com.yapp.project.report.domain.WeekReport;
import com.yapp.project.report.domain.WeekReportRepository;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final MissionRepository missionRepository;
    private final RoutineRepository routineRepository;
    private final WeekReportRepository weekReportRepository;
    private final MonthRoutineReportRepository monthRoutineReportRepository;
    private final MissionService missionService;


    @Transactional
    public Message clickAlarmToggle(Account account) {
        account.clickAlarmToggle();
        return Message.of(StatusEnum.ACCOUNT_OK,AccountContent.CHANGE_ALARM_TOGGLE);
    }


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
        if(!RegexUtil.validPassword(password)){
            throw new PasswordInvalidException();
        }
        dbAccount.resetPassword(passwordEncoder,password);
        return Message.of(StatusEnum.ACCOUNT_OK, AccountContent.ACCOUNT_OK_MSG);
    }

    @Transactional
    public Message removeAccount(Account account) {
        List<Mission> list = missionRepository.findAllByAccount(account);
        missionService.deleteMyMissionIncludeCaptures(list);
        List<Routine> routineList = routineRepository.findAllByIsDeleteIsFalseAndAccount(account);
        List<WeekReport> weekReportList = weekReportRepository.findAllByAccount(account);
        List<MonthRoutineReport> monthRoutineReportList = monthRoutineReportRepository.findAllByAccount(account);
        deleteRoutine(routineList);
        deleteWeekReport(weekReportList);
        deleteMonthReport(monthRoutineReportList);
        accountRepository.delete(account);
        return Message.of(StatusEnum.ACCOUNT_OK, AccountContent.ACCOUNT_OK_MSG);
    }

    private void deleteMonthReport(List<MonthRoutineReport> monthRoutineReportList) {
        monthRoutineReportList.forEach(MonthRoutineReport::deleteReport);
        monthRoutineReportRepository.saveAll(monthRoutineReportList);
    }

    private void deleteWeekReport(List<WeekReport> weekReportList) {
        weekReportList.forEach(WeekReport::deleteReport);
        weekReportRepository.saveAll(weekReportList);
    }

    private void deleteRoutine(List<Routine> routineList) {
        routineList.forEach(Routine::deleteRoutine);
        routineRepository.saveAll(routineList);
    }
}
