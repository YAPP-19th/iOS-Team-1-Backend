package com.yapp.project.routine.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.routine.BadRequestRoutineException;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.routine.domain.*;
import com.yapp.project.config.exception.routine.NotFoundRoutineException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;

    private final RetrospectRepository retrospectRepository;


    @Transactional(readOnly = true)
    public void getRoutineDaysRate(Account account) {
        List<Retrospect> retrospectList = retrospectRepository.findAllByDateBetweenAndRoutineAccount(
                LocalDate.parse("2021-10-18"), LocalDate.parse("2021-10-24"), account);
        List<Routine> routineList = routineRepository.findAllByIsDeleteIsFalseAndAccount(account);

        List<RoutineDTO.ResponseRoutineDaysRate> daysRateList = new ArrayList<>();
        LocalDate start = LocalDate.parse("2021-10-18");
        daysRateList.add(RoutineDTO.ResponseRoutineDaysRate.builder().date(start).build());
        for (int i = 1; i < 7; i++) {
            daysRateList.add(RoutineDTO.ResponseRoutineDaysRate.builder().date(
                    start.plusDays(i)).build());
        }

        routineList.forEach( x -> {
            LocalDate routineCreate = x.getCreatedAt().toLocalDate();
            x.getDays().forEach( y -> {
                if(y.getDay().equals(Week.MON)) {
                    if(routineCreate.isBefore(daysRateList.get(0).getDate()) || routineCreate.isEqual(daysRateList.get(0).getDate()))
                        daysRateList.get(0).updateTotalDate();
                } else if(y.getDay().equals(Week.TUE)) {
                    if(routineCreate.isBefore(daysRateList.get(1).getDate()) || routineCreate.isEqual(daysRateList.get(1).getDate()))
                        daysRateList.get(1).updateTotalDate();
                } else if(y.getDay().equals(Week.WED)) {
                    if(routineCreate.isBefore(daysRateList.get(2).getDate()) || routineCreate.isEqual(daysRateList.get(2).getDate()))
                        daysRateList.get(2).updateTotalDate();
                } else if(y.getDay().equals(Week.THU)) {
                    if(routineCreate.isBefore(daysRateList.get(3).getDate()) || routineCreate.isEqual(daysRateList.get(3).getDate()))
                        daysRateList.get(3).updateTotalDate();
                } else if(y.getDay().equals(Week.FRI)) {
                    if(routineCreate.isBefore(daysRateList.get(4).getDate()) || routineCreate.isEqual(daysRateList.get(4).getDate()))
                        daysRateList.get(4).updateTotalDate();
                } else if(y.getDay().equals(Week.SAT)) {
                    if(routineCreate.isBefore(daysRateList.get(5).getDate()) || routineCreate.isEqual(daysRateList.get(5).getDate()))
                        daysRateList.get(5).updateTotalDate();
                } else if(y.getDay().equals(Week.SUN)) {
                    if(routineCreate.isBefore(daysRateList.get(6).getDate()) || routineCreate.isEqual(daysRateList.get(6).getDate()))
                        daysRateList.get(6).updateTotalDate();
                }
            });
        });

        System.out.println("-------------------");
        daysRateList.forEach( x -> {
            retrospectList.forEach( y -> {
                if(y.getDate().isEqual(x.getDate())) {
                    if(y.getResult() == Result.DONE)
                        x.updateFullyDone();
                    else if(y.getResult() == Result.TRY)
                        x.updatePartiallyDone();
                }
            });
        });

        System.out.println("-------------------");
        daysRateList.forEach(x -> {
            System.out.println(x.getFullyDone() + ", " + x.getPartiallyDone() * 0.5 + ", " + x.getTotalDate());
            System.out.println(String.format("%.0f", ((double)(x.getFullyDone() + (x.getPartiallyDone() * 0.5)) / x.getTotalDate())*100) + '%');
        });
    }


    public RoutineDTO.ResponseRoutineListMessageDto updateRoutineSequence(Week day, ArrayList<Long> sequence, Account account) {
        List<Routine> routineList = findAllIsExistById(sequence);
        routineList.stream().forEach(x -> checkIsMine(account, x));
        updateRoutineDaysSequence(day, sequence, routineList);
        routineRepository.saveAll(routineList);
        return getRoutineList(day, account);
    }

    public Message deleteRoutine(Long routineId, Account account) {
        Routine routine = findIsExistByIdAndIsNotDelete(routineId);
        checkIsMine(account, routine);
        routine.deleteRoutine();
        routineRepository.save(routine);
        return Message.builder().msg("삭제 성공").status(StatusEnum.ROUTINE_OK).build();
    }

    public RoutineDTO.ResponseRoutineMessageDto updateRoutine(Long routineId, RoutineDTO.RequestRoutineDto updateRoutine, Account account) {
        checkDataIsNull(updateRoutine);
        Routine routine = findIsExistByIdAndIsNotDelete(routineId);
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
                .findAllByIsDeleteIsFalseAndAccountAndDaysDayOrderByDaysSequence(account, day, Sort.by("days").descending());
        return RoutineDTO.ResponseRoutineListMessageDto.builder()
                .message(Message.builder().msg("요일별 조회 성공").status(StatusEnum.ROUTINE_OK).build())
                .data(routineList.stream().map(routine -> RoutineDTO.ResponseRoutineDto.builder()
                        .routine(routine).build()).collect(Collectors.toList()))
                .build();
    }

    public RoutineDTO.ResponseRoutineMessageDto getRoutine(Long routineId, Account account) {
        Routine routine = findIsExistByIdAndIsNotDelete(routineId);
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
                newRoutine.getStartTime().isBlank()) throw new BadRequestRoutineException();
        return true;
    }

    private void setDays(List<Week> days, Routine routine) {
        List<RoutineDay> newDays = days.stream().map(day -> RoutineDay.builder().day(day).sequence(0L).routine(routine).build()).collect(Collectors.toList());
        routine.addDays(newDays);
    }

    public void checkIsMine(Account account, Routine routine) {
        if(!account.getId().equals(routine.getAccount().getId())) throw new BadRequestRoutineException();
    }

    public Routine findIsExistByIdAndIsNotDelete(Long routineId) {
        return routineRepository.findByIdAndIsDeleteIsFalse(routineId).orElseThrow(() -> new NotFoundRoutineException());
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