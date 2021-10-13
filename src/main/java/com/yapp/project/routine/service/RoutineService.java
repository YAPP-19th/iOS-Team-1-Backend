package com.yapp.project.routine.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.Content;
import com.yapp.project.routine.domain.*;
import com.yapp.project.config.exception.routine.BadRequestException;
import com.yapp.project.config.exception.routine.NotFoundRoutineException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    public ResponseEntity deleteRoutine(Long routineId, Account account) {
        Routine routine = findIsExist(routineId);
        checkIsMine(account, routine);
        routineRepository.delete(routine);
        return ResponseEntity.ok().build();
    }

    public RoutineDTO.ResponseRoutineDto updateRoutine(Long routineId, RoutineDTO.RequestRoutineDto updateRoutine, Account account) {
        checkDataIsNull(updateRoutine);
        Routine routine = findIsExist(routineId);
        checkIsMine(account, routine);
        routine.updateRoutine(updateRoutine);
        updateDayList(updateRoutine, routine);
        return RoutineDTO.ResponseRoutineDto.builder()
                .routine(routineRepository.save(routine)).build();
    }

    public List<RoutineDTO.ResponseRoutineDto> getRoutineList(Week day, Account account) {
        List<Routine> routineList = routineRepository // Sort.by("days").descending(): sequence가 0인 루틴은 최신 등록순
                .findAllByAccountAndDaysDayOrderByDaysSequence(account, day, Sort.by("days").descending());
        return routineList.stream().map(routine -> RoutineDTO.ResponseRoutineDto.builder()
                .routine(routine).build()).collect(Collectors.toList());
    }

    public RoutineDTO.ResponseRoutineDto getRoutine(Long routineId, Account account) {
        Routine routine = findIsExist(routineId);
        checkIsMine(account, routine);
        return RoutineDTO.ResponseRoutineDto.builder()
                .routine(routine).build();
    }

    public RoutineDTO.ResponseRoutineDto createRoutine(RoutineDTO.RequestRoutineDto newRoutine, Account account) {
        checkDataIsNull(newRoutine);
        Routine routine = Routine.builder()
                .account(account)
                .newRoutine(newRoutine).build();
        setDays(newRoutine.getDays(), routine);

        return RoutineDTO.ResponseRoutineDto.builder()
                .routine(routineRepository.save(routine)).build();
    }

    private Boolean checkDataIsNull(RoutineDTO.RequestRoutineDto newRoutine) {
        if( newRoutine.getTitle().isBlank() ||
                newRoutine.getGoal().isBlank() ||
                newRoutine.getDays().isEmpty() ||
                newRoutine.getStartTime().isBlank()) throw new BadRequestException(Content.BAD_REQUEST_CREATE_ROUTINE_DATA, StatusEnum.BAD_REQUEST);
        return true;
    }

    private void setDays(List<Week> days, Routine routine) {
        List<RoutineDay> newDays = days.stream().map(day -> RoutineDay.builder().day(day).sequence(0L).routine(routine).build()).collect(Collectors.toList());
        routine.addDays(newDays);
    }

    private void checkIsMine(Account account, Routine routine) {
        if(!account.getId().equals(routine.getAccount().getId())) throw new BadRequestException(Content.BAD_REQUEST_GET_ROUTINE_ID, StatusEnum.BAD_REQUEST);
    }

    private Routine findIsExist(Long routineId) {
        return routineRepository.findById(routineId).orElseThrow(() -> new NotFoundRoutineException(Content.NOT_FOUND_ROUTINE, StatusEnum.NOT_FOUND));
    }

    private void updateDayList(RoutineDTO.RequestRoutineDto updateRoutine, Routine routine) {
        List<RoutineDay> deleteDay = new ArrayList<>();
        routine.getDays().stream().forEach(x -> {
            if (!updateRoutine.getDays().contains(x.getDay()))
                deleteDay.add(x);
            else {
                updateRoutine.getDays().remove(x.getDay());
            }
        });
        routine.getDays().removeAll(deleteDay);
        setDays(updateRoutine.getDays(), routine);
    }
}