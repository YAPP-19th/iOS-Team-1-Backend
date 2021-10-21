package com.yapp.project.retrospect.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.retrospect.NotFoundRetrospectException;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.retrospect.domain.dto.RetrospectDTO;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.domain.RoutineRepository;
import com.yapp.project.routine.domain.Week;
import com.yapp.project.routine.service.RoutineService;
import com.yapp.project.snapshot.domain.Snapshot;
import com.yapp.project.snapshot.domain.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetrospectService {
    private static final String FILE_SERVER_PATH = "/Users/sol/Desktop/yapp19th-iOS1/image/retrospect/";

    private final RetrospectRepository retrospectRepository;
    private final SnapshotRepository snapshotRepository;
    private final RoutineRepository routineRepository;
    private final RoutineService routineService;


    public RetrospectDTO.RequestRetrospectListMessage getRetrospectList(Week day, LocalDate date, Account account) {
        List<Routine> allByAccountAndDaysDay = routineRepository.findAllByAccountAndDaysDayAndRetrospectsDate(account, day, date);
        List<Retrospect> retrospectList = allByAccountAndDaysDay.stream().map(x -> x.getRetrospects().get(0)).collect(Collectors.toList());
        return makeRetrospectListMessage(retrospectList, "요일 기준 회고 전체 조회 성공", StatusEnum.RETROSPECT_OK);
    }

    public RetrospectDTO.RequestRetrospectMessage getRetrospect(Long retrospectId, Account account) {
        Retrospect retrospect = retrospectRepository.findById(retrospectId).orElseThrow(NotFoundRetrospectException::new);
        routineService.checkIsMine(account, retrospect.getRoutine());
        return makeRetrospectMessage(retrospect, "회고 단일 조회 성공", StatusEnum.RETROSPECT_OK);
    }

    public Message deleteRetrospect(Long retrospectId, Account account) {
        Retrospect retrospect = retrospectRepository.findById(retrospectId).orElseThrow(NotFoundRetrospectException::new);
        routineService.checkIsMine(account, retrospect.getRoutine());
        retrospectRepository.delete(retrospect);
        return Message.builder().msg("삭제 성공").status(StatusEnum.RETROSPECT_OK).build();
    }

    public RetrospectDTO.RequestRetrospectMessage createRetrospect(RetrospectDTO.RequestRetrospect requestRetrospect, String imagePath ,Account account) {
        Routine routine = routineService.findIsExistByIdAndIsNotDelete(requestRetrospect.getRoutineId());
        routineService.checkIsMine(account, routine);
        Optional<Retrospect> preRetrospect = retrospectRepository.findByRoutineAndDate(routine, LocalDate.now());
        Retrospect retrospect = preRetrospect.orElseGet(() ->
                Retrospect.builder().routine(routine).content(requestRetrospect.getContent())
                        .isReport(false).result(Result.NOT).build());
        if(imagePath != null) retrospect.updateRetrospect(
                requestRetrospect.getContent(),
                snapshotRepository.save(Snapshot.builder().url(imagePath).build()));
        else retrospect.updateRetrospect(requestRetrospect.getContent());
        retrospectRepository.save(retrospect);
        return makeRetrospectMessage(retrospect, "회고 작성 성공", StatusEnum.RETROSPECT_OK);
    }

    public RetrospectDTO.RequestRetrospectMessage updateRetrospect(RetrospectDTO.RequestUpdateRetrospect updateRetrospect, Account account) throws IOException {
        Retrospect retrospect = retrospectRepository.findById(updateRetrospect.getRetrospectId()).orElseThrow(NotFoundRetrospectException::new);
        routineService.checkIsMine(account, retrospect.getRoutine());
        if(updateRetrospect.getImage() == null) retrospect.deleteImage();
        else {
            String newImagePath = saveImages(updateRetrospect.getImage(), retrospect.getRoutine().getId());
            if(!snapshotRepository.findByUrl(newImagePath).isPresent())
                retrospect.updateRetrospect(snapshotRepository.save(Snapshot.builder().url(newImagePath).build()));
        }
        retrospect.updateRetrospect(updateRetrospect.getContent());
        retrospectRepository.save(retrospect);
        return makeRetrospectMessage(retrospect, "회고 수정 성공", StatusEnum.RETROSPECT_OK);
    }

    public String saveImages(MultipartFile image, Long id) throws IOException {
        int dotIndex = image.getOriginalFilename().lastIndexOf(".");
        String fileName = image.getOriginalFilename().substring(0, dotIndex);
        String extension = image.getOriginalFilename().substring(dotIndex);
        String saveFileName = fileName + "_" + LocalDate.now() + extension;
        String SAVE_PATH = FILE_SERVER_PATH + id + "/";
        // Todo 추후 S3 전환 시, 아래 5라인 및 경로 수정, FILE_SERVER_PATH도 수정해야함.
        File path = new File(SAVE_PATH);
        if(!path.exists()) path.mkdir();
        image.transferTo(new File(SAVE_PATH, saveFileName));
        return SAVE_PATH + saveFileName;
    }

    private RetrospectDTO.RequestRetrospectMessage makeRetrospectMessage(Retrospect retrospect, String msg, StatusEnum status) {
        return RetrospectDTO.RequestRetrospectMessage.builder()
                .message(Message.builder().msg(msg).status(status).build())
                .data(RetrospectDTO.ResponseRetrospect.builder()
                        .retrospect(retrospect)
                        .routine(RoutineDTO.ResponseRoutineDto.builder()
                                .routine(retrospect.getRoutine()).build()).build()).build();
    }

    private RetrospectDTO.RequestRetrospectListMessage makeRetrospectListMessage(List<Retrospect> retrospectList, String msg, StatusEnum status) {
        List<RetrospectDTO.ResponseRetrospect> getRetrospectList = retrospectList.stream().map(x ->
                RetrospectDTO.ResponseRetrospect.builder()
                        .retrospect(x)
                        .routine(RoutineDTO.ResponseRoutineDto.builder()
                                .routine(x.getRoutine()).build()).build()).collect(Collectors.toList());
        return RetrospectDTO.RequestRetrospectListMessage.builder()
                .message(Message.builder().msg(msg).status(status).build())
                .data(getRetrospectList).build();
    }
}
