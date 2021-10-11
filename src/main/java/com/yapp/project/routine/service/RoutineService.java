package com.yapp.project.routine.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.routine.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    public List<RoutineDTO.ResponseRoutineDto> getRoutineList(Week day, Account account) {
        List<Routine> routineList = routineRepository // Sort.by("days").descending(): sequence가 0인 루틴은 최신 등록순
                .findAllByAccountAndDaysDayOrderByDaysSequence(account, day, Sort.by("days").descending());
        return routineList.stream().map(routine -> RoutineDTO.ResponseRoutineDto.builder()
                .routine(routine).build()).collect(Collectors.toList());
    }

    public RoutineDTO.ResponseRoutineDto getRoutine(Long routineId, Account account) throws BindException {
        Routine routine = findIsExist(routineId);
        checkIsMine(account, routine);
        return RoutineDTO.ResponseRoutineDto.builder()
                .routine(routine).build();
    }

    public RoutineDTO.ResponseRoutineDto createRoutine(RoutineDTO.RequestRoutineDto newRoutine, Account account) throws BindException {
        checkDataIsNull(newRoutine);
        Routine routine = Routine.builder()
                .account(account)
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

    private void checkIsMine(Account account, Routine routine) throws BindException {
        if(!account.getId().equals(routine.getAccount().getId())) throw new BindException(" ","Routine");
    }

    private Routine findIsExist(Long routineId) throws BindException {
        return routineRepository.findById(routineId).orElseThrow(() -> new BindException(" ","Routine"));
    }
}