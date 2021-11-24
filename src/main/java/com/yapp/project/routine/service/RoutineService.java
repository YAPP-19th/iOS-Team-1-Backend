package com.yapp.project.routine.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.config.exception.report.RoutineStartDayBadRequestException;
import com.yapp.project.config.exception.routine.BadRequestRoutineException;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.domain.repository.OrganizationRepository;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.routine.domain.*;
import com.yapp.project.config.exception.routine.NotFoundRoutineException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final RetrospectRepository retrospectRepository;
    private final OrganizationRepository organizationRepository;
    private static final int WEEK_LENGTH = 7;


    @Transactional(readOnly = true)
    public RoutineDTO.ResponseRecommendedRoutineMessageDto getRecommendedRoutine() {
        List<Organization> organList = organizationRepository.findAll();
        List<Organization> recommendedList = organList.stream().filter(organization ->
                !organization.getCategory().equals("기상")).collect(Collectors.toList());
        return RoutineDTO.ResponseRecommendedRoutineMessageDto.of(recommendedList);
    }

    @Transactional(readOnly = true)
    public RoutineDTO.ResponseDaysRoutineRateMessageDto getRoutineDaysRate(Account account, LocalDate start) {
        checkIsMonDay(start);
        List<Retrospect> retrospectList = retrospectRepository.findAllByDateBetweenAndRoutineAccount(
                start, start.plusDays(6), account);
        List<Routine> routineList = routineRepository.findAllByIsDeleteIsFalseAndAccount(account);
        List<RoutineDTO.ResponseRoutineDaysRate> daysRateList = new ArrayList<>();
        for (int i = 0; i < WEEK_LENGTH; i++) {
            daysRateList.add(RoutineDTO.ResponseRoutineDaysRate.builder().date(
                    start.plusDays(i)).build());
        }
        calculateDayRoutineAllCount(routineList, daysRateList);
        statisticsDayRoutine(retrospectList, daysRateList);
        return RoutineDTO.ResponseDaysRoutineRateMessageDto.of(daysRateList);
    }

    @Transactional
    public RoutineDTO.ResponseRoutineListMessageDto updateRoutineSequence(Week day, ArrayList<Long> sequence, Account account) {
        List<Routine> routineList = findAllIsExistById(sequence);
        routineList.forEach(x -> checkIsMine(account, x));
        updateRoutineDaysSequence(day, sequence, routineList);
        routineRepository.saveAll(routineList);
        return getRoutineList(day, account);
    }

    @Transactional
    public Message deleteRoutine(Long routineId, Account account) {
        Routine routine = findIsExistByIdAndIsNotDelete(routineId);
        checkIsMine(account, routine);
        routine.deleteRoutine();
        routineRepository.save(routine);
        return Message.builder().msg("삭제 성공").status(StatusEnum.ROUTINE_OK).build();
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public RoutineDTO.ResponseRoutineListMessageDto getRoutineList(Week day, Account account) {
        List<Routine> routineList = routineRepository // Sort.by("days").descending(): sequence가 0인 루틴은 최신 등록순
                .findAllByIsDeleteIsFalseAndAccountAndDaysDayOrderByDaysSequence(account, day, Sort.by("days").descending());
        return RoutineDTO.ResponseRoutineListMessageDto.builder()
                .message(Message.builder().msg("요일별 조회 성공").status(StatusEnum.ROUTINE_OK).build())
                .data(routineList.stream().map(routine -> RoutineDTO.ResponseRoutineDto.builder()
                        .routine(routine).build()).collect(Collectors.toList()))
                .build();
    }

    @Transactional(readOnly = true)
    public RoutineDTO.ResponseRoutineMessageDto getRoutine(Long routineId, Account account) {
        Routine routine = findIsExistByIdAndIsNotDelete(routineId);
        checkIsMine(account, routine);
        return RoutineDTO.ResponseRoutineMessageDto.builder()
                .message(Message.builder().msg("조회 성공").status(StatusEnum.ROUTINE_OK).build())
                .data(RoutineDTO.ResponseRoutineDto.builder().routine(routine).build())
                .build();
    }

    @Transactional
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
        return routineRepository.findByIdAndIsDeleteIsFalse(routineId).orElseThrow(NotFoundRoutineException::new);
    }

    private void updateDayList(RoutineDTO.RequestRoutineDto updateRoutine, Routine routine) {
        List<RoutineDay> deleteDay = new ArrayList<>();
        routine.getDays().forEach(day -> {
            if (!updateRoutine.getDays().contains(day.getDay()))
                deleteDay.add(day);
            else {
                updateRoutine.getDays().remove(day.getDay());
            }
        });
        routine.getDays().removeAll(deleteDay);
        setRetrospectIsReportTrueByDeleteDay(routine, deleteDay);
        setDays(updateRoutine.getDays(), routine);
    }

    private void setRetrospectIsReportTrueByDeleteDay(Routine routine, List<RoutineDay> deleteDay) {
        LocalDate start = DateUtil.KST_LOCAL_DATE_NOW().with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        List<Retrospect> retrospectList = retrospectRepository.findAllByDateBetweenAndRoutine(start, start.plusDays(6), routine);
        List<String> stringDayList = deleteDay.stream().map(day -> day.getDay().toString()).collect(Collectors.toList());
        List<Retrospect> deleteDayRetrospectList = retrospectList.stream().filter(retrospect ->
                        stringDayList.contains(retrospect.getDate()
                                .getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase()))
                .collect(Collectors.toList());
        retrospectList.removeAll(deleteDayRetrospectList);
        deleteDayRetrospectList.forEach(Retrospect::updateIsReportTrue);
        retrospectList.forEach(Retrospect::updateIsReportFalse);
    }

    private List<Routine> findAllIsExistById(ArrayList<Long> sequence) {
        return routineRepository.findAllById(sequence);
    }

    private void updateRoutineDaysSequence(Week day, ArrayList<Long> sequence, List<Routine> routineList) {
        HashMap<Long, Long> routineListSequence = new HashMap<>();
        long routineSequence = 1L;
        for (Long seq : sequence) {
            routineListSequence.put(seq, routineSequence++);
        }
        routineList.forEach(x -> x.getDays().forEach(y -> {
            if(y.getDay().equals(day))
                y.updateSequence(routineListSequence.get(x.getId()));
        }));
    }

    private void statisticsDayRoutine(List<Retrospect> retrospectList, List<RoutineDTO.ResponseRoutineDaysRate> daysRateList) {
        daysRateList.forEach(daysRate -> retrospectList.forEach(retrospect -> {
            if(retrospect.getDate().isEqual(daysRate.getDate())) {
                if(retrospect.getResult() == Result.DONE)
                    daysRate.updateFullyDone();
                else if(retrospect.getResult() == Result.TRY)
                    daysRate.updatePartiallyDone();
            }
        }));
    }

    private void calculateDayRoutineAllCount(List<Routine> routineList, List<RoutineDTO.ResponseRoutineDaysRate> daysRateList) {
        routineList.forEach(routine -> {
            LocalDate routineCreate = routine.getCreatedAt().toLocalDate();
            routine.getDays().forEach( routineDay -> {
                int index = routineDay.getDay().getIndex();
                if(isBeforeWeekRoutine(daysRateList, routineCreate, index))
                    daysRateList.get(index).updateTotalDone();
            });
        });
    }

    private boolean isBeforeWeekRoutine(List<RoutineDTO.ResponseRoutineDaysRate> daysRateList, LocalDate routineCreate, int index) {
        return routineCreate.isBefore(daysRateList.get(index).getDate()) || routineCreate.isEqual(daysRateList.get(index).getDate());
    }

    private void checkIsMonDay(LocalDate start) {
        Week isMon = Week.valueOf(start.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase());
        if(!isMon.equals(Week.MON)){
            throw new RoutineStartDayBadRequestException();
        }
    }
}