package com.yapp.project.retrospect.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
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
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATE_NOW;
import static com.yapp.project.aux.common.SnapShotUtil.saveImages;
import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
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
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATE_NOW;
import static com.yapp.project.aux.common.SnapShotUtil.saveImages;

@Service
@RequiredArgsConstructor
public class RetrospectService {
    private static final String FILE_SERVER_PATH = "/home/image/retrospect/";

    private final RetrospectRepository retrospectRepository;
    private final SnapshotRepository snapshotRepository;
    private final RoutineService routineService;

    public RetrospectDTO.ResponseRetrospectMessage setRetrospectResult(RetrospectDTO.RequestRetrospectResult retrospectResult, Account account) {
        Routine routine = routineService.findIsExistByIdAndIsNotDelete(retrospectResult.getRoutineId());
        routineService.checkIsMine(account, routine);
        checkIsDate(routine);
        Optional<Retrospect> preRetrospect = retrospectRepository.findByRoutineAndDate(routine, KST_LOCAL_DATE_NOW());
        Retrospect retrospect = preRetrospect.orElseGet(() ->
                Retrospect.builder().routine(routine).isReport(false).build());
        retrospect.updateResult(retrospectResult.getResult());
        Retrospect saveRetrospect = retrospectRepository.save(retrospect);
        return RetrospectDTO.ResponseRetrospectMessage.of(StatusEnum.RETROSPECT_OK , "회고 수행 여부 설정 성공", saveRetrospect);
    }

    public RetrospectDTO.ResponseRetrospectListMessage getRetrospectList(LocalDate date, Account account) {
        List<Retrospect> retrospectList = retrospectRepository.findAllByDateAndRoutineAccount(date, account);
        return RetrospectDTO.ResponseRetrospectListMessage.of(StatusEnum.RETROSPECT_OK, "요일 기준 회고 전체 조회 성공", retrospectList);
    }

    public RetrospectDTO.ResponseRetrospectMessage getRetrospect(Long retrospectId, Account account) {
        Retrospect retrospect = retrospectRepository.findById(retrospectId).orElseThrow(NotFoundRetrospectException::new);
        routineService.checkIsMine(account, retrospect.getRoutine());
        return RetrospectDTO.ResponseRetrospectMessage.of(StatusEnum.RETROSPECT_OK, "회고 단일 조회 성공", retrospect);
    }

    public Message deleteRetrospect(Long retrospectId, Account account) {
        Retrospect retrospect = retrospectRepository.findById(retrospectId).orElseThrow(NotFoundRetrospectException::new);
        routineService.checkIsMine(account, retrospect.getRoutine());
        retrospectRepository.delete(retrospect);
        return Message.builder().msg("삭제 성공").status(StatusEnum.RETROSPECT_OK).build();
    }

    @Transactional
    public RetrospectDTO.ResponseRetrospectMessage createRetrospect(RetrospectDTO.RequestRetrospect requestRetrospect, String imagePath ,Account account) {
        Routine routine = routineService.findIsExistByIdAndIsNotDelete(requestRetrospect.getRoutineId());
        routineService.checkIsMine(account, routine);
        Optional<Retrospect> preRetrospect = retrospectRepository.findByRoutineAndDate(routine, KST_LOCAL_DATE_NOW());
        Retrospect retrospect = preRetrospect.orElseGet(() ->
                Retrospect.builder().routine(routine).content(requestRetrospect.getContent())
                        .isReport(false).result(Result.NOT).build());
        if(imagePath != null) {
            retrospect.updateRetrospect(requestRetrospect.getContent(), snapshotRepository.save(Snapshot.builder().url(imagePath).build()));
        } else {
            retrospect.updateRetrospect(requestRetrospect.getContent());
        }
        Retrospect saveRetrospect = retrospectRepository.save(retrospect);
        return RetrospectDTO.ResponseRetrospectMessage.of(StatusEnum.RETROSPECT_OK, "회고 작성 성공", saveRetrospect);
    }

    @Transactional
    public RetrospectDTO.ResponseRetrospectMessage updateRetrospect(RetrospectDTO.RequestUpdateRetrospect updateRetrospect, Account account) throws IOException {
        Retrospect retrospect = retrospectRepository.findById(updateRetrospect.getRetrospectId()).orElseThrow(NotFoundRetrospectException::new);
        routineService.checkIsMine(account, retrospect.getRoutine());
        checkIsUpdateValidity(retrospect);
        if(updateRetrospect.getImage() == null) {
            retrospect.deleteImage();
        } else {
            String newImagePath = saveImages(updateRetrospect.getImage(), retrospect.getRoutine().getId(), FILE_SERVER_PATH);
            if(!snapshotRepository.findByUrl(newImagePath).isPresent()) {
                retrospect.updateRetrospect(snapshotRepository.save(Snapshot.builder().url(newImagePath).build()));
            }
        }
        retrospect.updateRetrospect(updateRetrospect.getContent());
        Retrospect saveRetrospect = retrospectRepository.save(retrospect);
        return RetrospectDTO.ResponseRetrospectMessage.of(StatusEnum.RETROSPECT_OK,"회고 수정 성공", saveRetrospect);
    }

    private void checkIsUpdateValidity(Retrospect retrospect) {
        LocalDate lastUpdatableDay = KST_LOCAL_DATE_NOW().minusDays(2);
        if(retrospect.getDate().isBefore(lastUpdatableDay)){
            throw new InvalidRetrospectUpdateException();
        }
    }

    private void checkIsDate(Routine routine) {
        DayOfWeek dayOfWeek = KST_LOCAL_DATE_NOW().getDayOfWeek();
        List<String> dateList = routine.getDays().stream().map(x -> x.getDay().toString()).collect(Collectors.toList());
        boolean isContains = dateList.contains(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.US).toUpperCase());
        if(!isContains) throw new BadRequestRetrospectException();
    }
}
