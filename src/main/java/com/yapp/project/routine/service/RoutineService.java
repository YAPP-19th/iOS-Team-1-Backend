package com.yapp.project.routine.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.routine.domain.*;
import com.yapp.project.routine.exception.BadRequestException;
import com.yapp.project.routine.exception.NotFoundRoutineException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
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
                newRoutine.getStartTime().isBlank()) throw new BadRequestException("요청 데이터를 확인하세요.", StatusEnum.BAD_REQUEST);
        return true;
    }

    private void setDays(List<Week> days, Routine routine) {
        List<RoutineDay> newDays = days.stream().map(day -> RoutineDay.builder().day(day).sequence(0L).build()).collect(Collectors.toList());
        newDays.stream().forEach(day -> routine.addDays(day));
    }

    private void checkIsMine(Account account, Routine routine) {
        if(!account.getId().equals(routine.getAccount().getId())) throw new BadRequestException("루틴ID를 확인하세요.", StatusEnum.BAD_REQUEST);
    }

    private Routine findIsExist(Long routineId) {
        return routineRepository.findById(routineId).orElseThrow(() -> new NotFoundRoutineException("루틴이 존재하지 않습니다.", StatusEnum.NOT_FOUND));
    }
}