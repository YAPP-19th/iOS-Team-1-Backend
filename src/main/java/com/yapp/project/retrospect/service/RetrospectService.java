package com.yapp.project.retrospect.service;

import com.google.cloud.storage.BlobInfo;
import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.storage.CloudStorageUtil;
import com.yapp.project.config.exception.retrospect.BadRequestRetrospectException;
import com.yapp.project.config.exception.retrospect.InvalidRetrospectUpdateException;
import com.yapp.project.config.exception.retrospect.NotFoundRetrospectException;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.retrospect.domain.dto.RetrospectDTO;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.service.RoutineService;
import com.yapp.project.snapshot.domain.Snapshot;
import com.yapp.project.snapshot.domain.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATE_NOW;
import static com.yapp.project.aux.content.RetrospectContent.*;


@Service
@RequiredArgsConstructor
public class RetrospectService {

    @Value("${property.status}")
    private String STATUS;
    private static final String path = "/retrospect/";

    private final RetrospectRepository retrospectRepository;
    private final SnapshotRepository snapshotRepository;
    private final RoutineService routineService;
    private final CloudStorageUtil cloudStorageUtil;

    @Transactional
    public RetrospectDTO.ResponseRetrospectMessage setRetrospectResult(RetrospectDTO.RequestRetrospectResult retrospectResult, Account account) {
        Routine routine = routineService.findIsExistByIdAndIsNotDelete(retrospectResult.getRoutineId());
        routineService.checkIsMine(account, routine);
        checkIsDate(routine, LocalDate.parse(retrospectResult.getDate()));
        Optional<Retrospect> preRetrospect = retrospectRepository.findByRoutineAndDate(routine,
                LocalDate.parse(retrospectResult.getDate()));
        Retrospect retrospect = preRetrospect.orElseGet(() ->
                Retrospect.builder().routine(routine).isReport(false).date(retrospectResult.getDate()).build());
        retrospect.updateResult(retrospectResult.getResult());
        Retrospect saveRetrospect = retrospectRepository.save(retrospect);
        return RetrospectDTO.ResponseRetrospectMessage.of(StatusEnum.RETROSPECT_OK , RETROSPECT_RESULT_SET_OK, saveRetrospect);
    }

    @Transactional(readOnly = true)
    public RetrospectDTO.ResponseRetrospectListMessage getRetrospectList(LocalDate date, Account account) {
        List<Retrospect> retrospectList = retrospectRepository.findAllByDateAndRoutineAccount(date, account);
        return RetrospectDTO.ResponseRetrospectListMessage.of(StatusEnum.RETROSPECT_OK, RETROSPECT_GET_ALL_BY_DAY_OK, retrospectList);
    }

    @Transactional(readOnly = true)
    public RetrospectDTO.ResponseRetrospectMessage getRetrospect(Long retrospectId, Account account) {
        Retrospect retrospect = retrospectRepository.findById(retrospectId).orElseThrow(NotFoundRetrospectException::new);
        routineService.checkIsMine(account, retrospect.getRoutine());
        return RetrospectDTO.ResponseRetrospectMessage.of(StatusEnum.RETROSPECT_OK, RETROSPECT_GET_BY_ID_OK, retrospect);
    }

    @Transactional
    public Message deleteRetrospect(Long retrospectId, Account account) {
        Retrospect retrospect = retrospectRepository.findById(retrospectId).orElseThrow(NotFoundRetrospectException::new);
        routineService.checkIsMine(account, retrospect.getRoutine());
        retrospectRepository.delete(retrospect);
        return Message.builder().msg(RETROSPECT_DELETE_BY_OK).status(StatusEnum.RETROSPECT_OK).build();
    }

    @Transactional
    public RetrospectDTO.ResponseRetrospectMessage createRetrospect(RetrospectDTO.RequestRetrospect requestRetrospect, Account account) throws IOException {
        Routine routine = routineService.findIsExistByIdAndIsNotDelete(requestRetrospect.getRoutineId());
        routineService.checkIsMine(account, routine);
        checkIsDate(routine, LocalDate.parse(requestRetrospect.getDate()));
        Optional<Retrospect> preRetrospect = retrospectRepository.findByRoutineAndDate(routine,
                LocalDate.parse(requestRetrospect.getDate()));
        Retrospect retrospect = preRetrospect.orElseGet(() ->
                Retrospect.builder().routine(routine).content(requestRetrospect.getContent())
                        .isReport(false).result(Result.NOT).date(requestRetrospect.getDate()).build());
        if(requestRetrospect.getImage() != null) {
            BlobInfo image = cloudStorageUtil.upload(
                    requestRetrospect.getImage(), STATUS + path + requestRetrospect.getRoutineId() + "/");
            String imagePath = CloudStorageUtil.getImageURL(image);
            retrospect.updateRetrospect(requestRetrospect.getContent(), snapshotRepository.save(Snapshot.builder().url(imagePath).build()));
        } else {
            retrospect.updateRetrospect(requestRetrospect.getContent());
        }
        Retrospect saveRetrospect = retrospectRepository.save(retrospect);
        return RetrospectDTO.ResponseRetrospectMessage.of(StatusEnum.RETROSPECT_OK, RETROSPECT_CREATE_OK, saveRetrospect);
    }

    @Transactional
    public RetrospectDTO.ResponseRetrospectMessage updateRetrospect(RetrospectDTO.RequestUpdateRetrospect updateRetrospect, Account account) throws IOException {
        Retrospect retrospect = retrospectRepository.findById(updateRetrospect.getRetrospectId()).orElseThrow(NotFoundRetrospectException::new);
        routineService.checkIsMine(account, retrospect.getRoutine());
        checkIsUpdateValidity(retrospect);
        if(updateRetrospect.getImage() != null) {
            BlobInfo image = cloudStorageUtil.upload(
                    updateRetrospect.getImage(), STATUS + path + retrospect.getRoutine().getId() + "/");
            String imagePath = CloudStorageUtil.getImageURL(image);
            retrospect.updateRetrospect(snapshotRepository.save(Snapshot.builder().url(imagePath).build()));
        } else {
            retrospect.deleteImage();
        }
        retrospect.updateRetrospect(updateRetrospect.getContent());
        Retrospect saveRetrospect = retrospectRepository.save(retrospect);
        return RetrospectDTO.ResponseRetrospectMessage.of(StatusEnum.RETROSPECT_OK,RETROSPECT_UPDATE_OK, saveRetrospect);
    }

    private void checkIsUpdateValidity(Retrospect retrospect) {
        LocalDate lastUpdatableDay = KST_LOCAL_DATE_NOW().minusDays(2);
        if(retrospect.getDate().isBefore(lastUpdatableDay)){
            throw new InvalidRetrospectUpdateException();
        }
    }

    private void checkIsDate(Routine routine, LocalDate date) {
        boolean isDayContains = checkDateIsInRoutineDate(routine, date);
        boolean isBeforeTwoDayFromNow = checkDateIsBeforeTwoDayFromNow(date);
        if(!isDayContains || !isBeforeTwoDayFromNow)
            throw new BadRequestRetrospectException();
    }

    private boolean checkDateIsInRoutineDate(Routine routine, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<String> dateList = routine.getDays().stream().map(x -> x.getDay().toString()).collect(Collectors.toList());
        return dateList.contains(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US).toUpperCase());
    }

    private boolean checkDateIsBeforeTwoDayFromNow(LocalDate date) {
        LocalDate plusTwoDay = date.plusDays(2);
        LocalDate now = KST_LOCAL_DATE_NOW();
        return now.isBefore(plusTwoDay) || now.isEqual(plusTwoDay);
    }
}
