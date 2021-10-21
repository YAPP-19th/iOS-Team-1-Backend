package com.yapp.project.routine.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.routine.RoutineContent;
import com.yapp.project.routine.domain.*;
import com.yapp.project.config.exception.routine.BadRequestException;
import com.yapp.project.config.exception.routine.NotFoundRoutineException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    public RoutineDTO.ResponseRoutineListMessageDto updateRoutineSequence(Week day, ArrayList<Long> sequence, Account account) {
        List<Routine> routineList = findAllIsExistById(sequence);
        routineList.stream().forEach(x -> checkIsMine(account, x));
        updateRoutineDaysSequence(day, sequence, routineList);
        routineRepository.saveAll(routineList);
        return getRoutineList(day, account);
    }

    public Message deleteRoutine(Long routineId, Account account) {
        Routine routine = findIsExistById(routineId);
        checkIsMine(account, routine);
        routine.deleteRoutine();
        routineRepository.save(routine);
        return Message.builder().msg("삭제 성공").status(StatusEnum.ROUTINE_OK).build();
    }

    public RoutineDTO.ResponseRoutineMessageDto updateRoutine(Long routineId, RoutineDTO.RequestRoutineDto updateRoutine, Account account) {
        checkDataIsNull(updateRoutine);
        Routine routine = findIsExistById(routineId);
        checkIsMine(account, routine);
        routine.updateRoutine(updateRoutine);
        updateDayList(updateRoutine, routine);
        return RoutineDTO.ResponseRoutineMessageDto.builder()
                .message(Message.builder().msg("수정 성공").status(StatusEnum.ROUTINE_OK).build())
                .data(RoutineDTO.ResponseRoutineDto.builder().routine(routineRepository.save(routine)).build())
                .build();
    }

    public RoutineDTO.ResponseRoutineListMessageDto getRoutineList(Week day, Account account) {
        List<Routine> routineList = routineRepository // Sort.by("days").descending(): sequence가 0인 루틴은 최신 등록순
                .findAllByAccountAndDaysDayOrderByDaysSequence(account, day, Sort.by("days").descending());
        return RoutineDTO.ResponseRoutineListMessageDto.builder()
                .message(Message.builder().msg("요일별 조회 성공").status(StatusEnum.ROUTINE_OK).build())
                .data(routineList.stream().map(routine -> RoutineDTO.ResponseRoutineDto.builder()
                        .routine(routine).build()).collect(Collectors.toList()))
                .build();
    }

    public RoutineDTO.ResponseRoutineMessageDto getRoutine(Long routineId, Account account) {
        Routine routine = findIsExistById(routineId);
        checkIsMine(account, routine);
        return RoutineDTO.ResponseRoutineMessageDto.builder()
                .message(Message.builder().msg("조회 성공").status(StatusEnum.ROUTINE_OK).build())
                .data(RoutineDTO.ResponseRoutineDto.builder().routine(routine).build())
                .build();
    }

    public RoutineDTO.ResponseRoutineMessageDto createRoutine(RoutineDTO.RequestRoutineDto newRoutine, Account account) {
        checkDataIsNull(newRoutine);
        Routine routine = Routine.builder()
                .account(account)
                .newRoutine(newRoutine).build();
        setDays(newRoutine.getDays(), routine);
        return RoutineDTO.ResponseRoutineMessageDto.builder()
                .message(Message.builder().msg("생성 성공").status(StatusEnum.ROUTINE_OK).build())
                .data(RoutineDTO.ResponseRoutineDto.builder().routine(routineRepository.save(routine)).build())
                .build();
    }

    private Boolean checkDataIsNull(RoutineDTO.RequestRoutineDto newRoutine) {
        if( newRoutine.getTitle().isBlank() ||
                newRoutine.getGoal().isBlank() ||
                newRoutine.getDays().isEmpty() ||
                newRoutine.getStartTime().isBlank()) throw new BadRequestException(RoutineContent.BAD_REQUEST_CREATE_ROUTINE_DATA, StatusEnum.ROUTINE_BAD_REQUEST);
        return true;
    }

    private void setDays(List<Week> days, Routine routine) {
        List<RoutineDay> newDays = days.stream().map(day -> RoutineDay.builder().day(day).sequence(0L).routine(routine).build()).collect(Collectors.toList());
        routine.addDays(newDays);
    }

    public void checkIsMine(Account account, Routine routine) {
        if(!account.getId().equals(routine.getAccount().getId())) throw new BadRequestException(RoutineContent.BAD_REQUEST_GET_ROUTINE_ID, StatusEnum.ROUTINE_BAD_REQUEST);
    }

    public Routine findIsExistById(Long routineId) {
        return routineRepository.findByIdAndIsDelete(routineId, false).orElseThrow(() -> new NotFoundRoutineException(RoutineContent.NOT_FOUND_ROUTINE, StatusEnum.ROUTINE_NOT_FOUND));
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

    private List<Routine> findAllIsExistById(ArrayList<Long> sequence) {
        return routineRepository.findAllById(sequence);
    }

    private void updateRoutineDaysSequence(Week day, ArrayList<Long> sequence, List<Routine> routineList) {
        HashMap<Long, Long> routineListSequence = new HashMap<>();
        Long routineSequence = 1L;
        for (Long seq : sequence) {
            routineListSequence.put(seq, routineSequence++);
        }
        routineList.forEach(x -> {
            x.getDays().forEach(y -> {
                if(y.getDay().equals(day))
                    y.updateSequence(routineListSequence.get(x.getId()));
            });
        });
    }
}