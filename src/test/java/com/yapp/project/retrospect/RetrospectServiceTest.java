package com.yapp.project.retrospect;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.config.exception.retrospect.BadRequestRetrospectException;
import com.yapp.project.config.exception.retrospect.NotFoundRetrospectException;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.retrospect.domain.dto.RetrospectDTO;
import com.yapp.project.retrospect.service.RetrospectService;
import com.yapp.project.routine.domain.*;
import com.yapp.project.routine.service.RoutineService;
import com.yapp.project.snapshot.domain.Snapshot;
import com.yapp.project.snapshot.domain.SnapshotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RetrospectServiceTest {

    @Mock
    private RoutineService routineService;
    @InjectMocks
    private RetrospectService retrospectService;
    @Mock
    private RetrospectRepository retrospectRepository;
    @Mock
    private SnapshotRepository snapshotRepository;

    @Test
    void testCreateRetrospectSuccess() {
        try(MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)) {
            // given
            Account account = AccountTemplate.makeTestAccount();
            List<Week> days = new ArrayList<>();
            days.add(Week.MON);
            days.add(Week.TUE);
            RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("?????????", "??????", days, "07:35", RoutineCategory.DAILY.getIndex());
            Routine fakeRoutine = Routine.builder().account(account).newRoutine(newRoutine).id(1L).build();
            List<RoutineDay> newDays = days.stream().map(day -> RoutineDay.builder().day(day).sequence(0L).routine(fakeRoutine).build()).collect(Collectors.toList());
            fakeRoutine.addDays(newDays);
            Retrospect fakeRetrospect = Retrospect.builder().routine(fakeRoutine).isReport(false).result(Result.NOT).date("2021-10-12").build();
            Retrospect fakeRetrospectSnapShot = Retrospect.builder().routine(fakeRoutine).isReport(false).result(Result.NOT).content("????????? ?????? ??????").date("2021-10-12").build();
            RetrospectDTO.RequestRetrospect fakeRequestRetrospect =
                    RetrospectDTO.RequestRetrospect.builder().routineId(1L).date("2021-10-12").content("????????? ?????? ??????").build();

            // mocking
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-10-12"));
            given(retrospectRepository.findByRoutineAndDate(fakeRoutine, LocalDate.parse("2021-10-12"))).willReturn(Optional.of(fakeRetrospect));
            given(retrospectRepository.save(any())).willReturn(fakeRetrospectSnapShot);
            given(routineService.findIsExistByIdAndIsNotDelete(1L)).willReturn(fakeRoutine);

            // when
            RetrospectDTO.ResponseRetrospect fakeResponseRetrospect = retrospectService.createRetrospect(fakeRequestRetrospect, account).getData();

            assertThat(fakeRequestRetrospect.getContent()).isEqualTo(fakeResponseRetrospect.getContent());
            assertThat(fakeRequestRetrospect.getRoutineId()).isEqualTo(fakeResponseRetrospect.getRoutine().getId());
        } catch (IOException e) {
        }
    }

//    @Test
//    void testSaveAndGetPathSuccess() {
//        // Todo ????????? ?????? ????????? ???????????? S3 ?????? ??? ??????
//        // ????????? ?????? ????????? ???????????? ???????????? ????????? ????????? ???????????? ?????? ??????
//    }

    @Test
    void testUpdateNonImageSuccess() throws IOException {
        try(MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)) {
            // given
            Account account = AccountTemplate.makeTestAccount();
            List<Week> days = new ArrayList<>();
            days.add(Week.MON);
            days.add(Week.TUE);
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-11-30"));
            RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("?????????", "??????", days, "07:35", RoutineCategory.DAILY.getIndex());
            Routine fakeRoutine = Routine.builder().account(account).newRoutine(newRoutine).id(1L).build();
            Retrospect fakeRetrospect = Retrospect.builder().routine(fakeRoutine).isReport(false).result(Result.NOT).date("2021-11-29").build();
            fakeRetrospect.updateRetrospect("????????? ?????? ??????");
            RetrospectDTO.RequestUpdateRetrospect fakeUpdateRetrospect = RetrospectDTO.RequestUpdateRetrospect.builder().retrospectId(1L).content("????????? ?????? ??????").build();

            Retrospect fakeRetrospect2 = Retrospect.builder().routine(fakeRoutine).isReport(false).result(Result.NOT).date("2021-11-29").build();
            fakeRetrospect2.updateRetrospect("????????? ?????? ??????");

            // mocking
            given(retrospectRepository.findById(1L)).willReturn(Optional.of(fakeRetrospect));
            given(retrospectRepository.save(any())).willReturn(fakeRetrospect2);

            // when
            RetrospectDTO.ResponseRetrospect fakeResponseRetrospect = retrospectService.updateRetrospect(fakeUpdateRetrospect, account).getData();

            // then
            assertThat(fakeRetrospect2.getContent()).isEqualTo(fakeResponseRetrospect.getContent());
        }
    }

    @Test
    void testUpdateRetrospectFailure(){
        // given
        Account account = AccountTemplate.makeTestAccount();
        RetrospectDTO.RequestUpdateRetrospect fakeUpdateRetrospect = RetrospectDTO.RequestUpdateRetrospect.builder().retrospectId(2L).content("????????? ?????? ??????").build();

        // when then
        assertThrows(NotFoundRetrospectException.class, () -> retrospectService.updateRetrospect(fakeUpdateRetrospect, account));
    }

    @Test
    void testSetResultRetrospect() {
        try(MockedStatic<DateUtil> dateUtil = Mockito.mockStatic(DateUtil.class)) {
            // given
            Account account = AccountTemplate.makeTestAccount();
            List<Week> days = new ArrayList<>();
            days.add(Week.MON); days.add(Week.THU); days.add(Week.WED); days.add(Week.TUE);
            days.add(Week.FRI); days.add(Week.SAT); days.add(Week.SUN);
            RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("?????????", "??????", days, "07:35", RoutineCategory.DAILY.getIndex());
            Routine fakeRoutine = Routine.builder().account(account).newRoutine(newRoutine).id(1L).build();
            List<RoutineDay> newDays = days.stream().map(day -> RoutineDay.builder().day(day).sequence(0L).routine(fakeRoutine).build()).collect(Collectors.toList());
            fakeRoutine.addDays(newDays);
            Retrospect fakeRetrospect = Retrospect.builder().routine(fakeRoutine).isReport(false).result(Result.NOT).date("2021-10-12").build();
            fakeRetrospect.updateRetrospect("????????? ?????? ??????");
            RetrospectDTO.RequestRetrospectResult fakeResult =
                    RetrospectDTO.RequestRetrospectResult.builder().routineId(1L).date("2021-10-12").result(Result.DONE).build();

            // mocking
            dateUtil.when(DateUtil::KST_LOCAL_DATE_NOW).thenReturn(LocalDate.parse("2021-10-12"));
            given(routineService.findIsExistByIdAndIsNotDelete(any())).willReturn(fakeRoutine);
            given(retrospectRepository.findByRoutineAndDate(fakeRoutine, LocalDate.parse("2021-10-12"))).willReturn(Optional.of(fakeRetrospect));
            given(retrospectRepository.save(any())).willReturn(fakeRetrospect);

            // when
            RetrospectDTO.ResponseRetrospectMessage responseRetrospectMessage = retrospectService.setRetrospectResult(fakeResult, account);

            // then
            assertThat(fakeResult.getResult()).isEqualTo(responseRetrospectMessage.getData().getResult());
        }
    }

    @Test
    void testSetResultNotDayRetrospect() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();

        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("?????????", "??????", days, "07:35", RoutineCategory.DAILY.getIndex());
        Routine fakeRoutine = Routine.builder().account(account).newRoutine(newRoutine).id(1L).build();
        Retrospect fakeRetrospect = Retrospect.builder().routine(fakeRoutine).isReport(false).result(Result.NOT).date("2021-10-12").build();
        fakeRetrospect.updateRetrospect("????????? ?????? ??????");
        RetrospectDTO.RequestRetrospectResult fakeResult =
                RetrospectDTO.RequestRetrospectResult.builder().routineId(1L).date("2021-10-12").result(Result.DONE).build();

        // mocking
        given(routineService.findIsExistByIdAndIsNotDelete(any())).willReturn(fakeRoutine);

        // when then
        assertThrows(BadRequestRetrospectException.class, () -> retrospectService.setRetrospectResult(fakeResult, account));
    }
}