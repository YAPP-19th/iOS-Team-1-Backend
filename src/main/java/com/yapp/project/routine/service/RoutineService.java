package com.yapp.project.routine.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.config.security.PrincipalDetails;
import com.yapp.project.routine.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    public RoutineDTO.ResponseRoutineDto createRoutine(RoutineDTO.RequestRoutineDto newRoutine) throws BindException {
        checkDataIsNull(newRoutine);
        Routine routine = Routine.builder()
                .account(getAccount())
                .newRoutine(newRoutine).build();
        setDays(newRoutine.getDays(), routine);

        return RoutineDTO.ResponseRoutineDto.builder()
                .routine(routineRepository.save(routine)).build();
    }

    private Boolean checkDataIsNull(RoutineDTO.RequestRoutineDto newRoutine) throws BindException {
        if( newRoutine.getTitle().isBlank() ||
                newRoutine.getGoal().isBlank() ||
                newRoutine.getDays().isEmpty() ||
                newRoutine.getStartTime().isBlank()) throw new BindException(" ","Routine");
        return true;
    }

    private void setDays(List<Week> days, Routine routine) {
        List<RoutineDay> newDays = days.stream().map(day -> RoutineDay.builder().day(day).sequence(0L).build()).collect(Collectors.toList());
        newDays.stream().forEach(day -> routine.addDays(day));
    }

    private Account getAccount() {
        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getAccount();
    }
}