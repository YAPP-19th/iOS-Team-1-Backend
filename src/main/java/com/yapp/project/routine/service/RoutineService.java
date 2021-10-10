package com.yapp.project.routine.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.base.Cron;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.domain.RoutineRepository;
import com.yapp.project.routine.domain.Week;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final AccountRepository accountRepository; // will delete

    public RoutineDTO.Response createRoutine(RoutineDTO.Request newRoutine) throws BindException {
        checkDataIsNull(newRoutine);
        Routine routine = Routine.builder()
                .account(getAccount())
                .newRoutine(newRoutine).build();
        setCron(newRoutine.getDays(), routine);

        return RoutineDTO.Response.builder()
                .routine(routineRepository.save(routine)).build();
    }

    private Boolean checkDataIsNull(RoutineDTO.Request newRoutine) throws BindException {
        if( newRoutine.getTitle().isBlank() ||
            newRoutine.getGoal().isBlank() ||
            newRoutine.getDays().isEmpty() ||
            newRoutine.getStartTime().isBlank()) throw new BindException(" ","Routine");
        return true;
    }

    private void setCron(List<Week> days, Routine routine) {
        List<Cron> crons = days.stream().map(cron -> Cron.builder().week(cron).build()).collect(Collectors.toList());
        crons.stream().forEach(cron -> routine.addCrons(cron));
    }

    // Todo - id Security Context에서 얻는 작업 마무리 되면 수정 예정
    private Account getAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account =  accountRepository.findByEmail(email).get();
        return account;
    }
}
