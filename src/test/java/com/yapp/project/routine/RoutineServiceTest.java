package com.yapp.project.routine;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.aux.test.organization.OrganizationTemplate;
import com.yapp.project.config.exception.report.RoutineStartDayBadRequestException;
import com.yapp.project.config.exception.routine.BadRequestRoutineException;
import com.yapp.project.organization.domain.Category;
import com.yapp.project.organization.domain.Organization;
import com.yapp.project.organization.service.GroupService;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.routine.domain.*;
import com.yapp.project.config.exception.routine.NotFoundRoutineException;
import com.yapp.project.routine.service.RoutineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.yapp.project.routine.domain.RoutineCategory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoutineServiceTest {

    @InjectMocks
    private RoutineService routineService;

    @Mock
    private RoutineRepository routineRepository;
    @Mock
    private RetrospectRepository retrospectRepository;
    @Mock
    private GroupService groupService;

    @Test
    void testCreateRoutineSuccess() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", DAILY.getIndex());
        Routine fakeRoutine = Routine.builder().account(account).newRoutine(newRoutine).build();

        // mocking
        given(routineRepository.save(any())).willReturn(fakeRoutine);
        // when
        RoutineDTO.ResponseRoutineDto routine = routineService.createRoutine(newRoutine, account).getData();
        // then
        assertThat(routine.getTitle()).isEqualTo(newRoutine.getTitle());
        assertThat(routine.getGoal()).isEqualTo(newRoutine.getGoal());
        assertThat(routine.getStartTime()).isEqualTo(newRoutine.getStartTime());
        assertThat(routine.getCategory()).isEqualTo(newRoutine.getCategory());
    }

    @Test
    void testCreateRoutineFailureBadRequest() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("", "", days, "07:35", DAILY.getIndex());

        // when then
        assertThrows(BadRequestRoutineException.class, () -> routineService.createRoutine(newRoutine, account));
    }

    @Test
    void testGetRoutineSuccess() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", DAILY.getIndex());
        Routine fakeRoutine = Routine.builder().account(account).newRoutine(newRoutine).build();

        // mocking
        given(routineRepository.findByIdAndIsDeleteIsFalse(1L)).willReturn(Optional.of(fakeRoutine));

        // when
        RoutineDTO.ResponseRoutineDto routine = routineService.getRoutine(1L, account).getData();
        // then
        assertThat(routine.getTitle()).isEqualTo(newRoutine.getTitle());
        assertThat(routine.getGoal()).isEqualTo(newRoutine.getGoal());
        assertThat(routine.getStartTime()).isEqualTo(newRoutine.getStartTime());
        assertThat(routine.getCategory()).isEqualTo(newRoutine.getCategory());
    }

    @Test
    void testGetRoutineFailureNotFound() {
        // given
        Account account = AccountTemplate.makeTestAccount();

        // when then
        assertThrows(NotFoundRoutineException.class, () -> routineService.getRoutine(1L, account));
    }

    @Test
    void testGetRoutineFailureBadRequest() {
        // given
        Account account1 = AccountTemplate.makeTestAccount();
        Account account2 = AccountTemplate.makeTestAccount("나는@두번째.사람");
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", DAILY.getIndex());
        Routine fakeRoutine = Routine.builder().account(account1).newRoutine(newRoutine).build();

        // mocking
        given(routineRepository.findByIdAndIsDeleteIsFalse(1L)).willReturn(Optional.of(fakeRoutine));

        // when then
        assertThrows(BadRequestRoutineException.class, () -> routineService.getRoutine(1L, account2));
    }

    @Test
    void testGetRoutineListSuccess() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();
        List<Routine> routines = new ArrayList<>();
        days.add(Week.MON); days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine1 = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", DAILY.getIndex());
        RoutineDTO.RequestRoutineDto newRoutine2 = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", DAILY.getIndex());
        routines.add(Routine.builder().account(account).newRoutine(newRoutine1).build());
        routines.add(Routine.builder().account(account).newRoutine(newRoutine2).build());

        // mocking
        given(routineRepository
                .findAllByIsDeleteIsFalseAndAccountAndDaysDayOrderByDaysSequence(account, Week.MON, Sort.by("days").descending())).willReturn(routines);
        // when
        List<RoutineDTO.ResponseRoutineDto> routineList = routineService.getRoutineList(Week.MON, account).getData();
        // then
        assertThat(routineList.size()).isEqualTo(routines.size());
    }

    @Test
    void testUpdateRoutineSuccess() {
        //given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();
        List<Week> newDays = new ArrayList<>();
        days.add(Week.MON); newDays.add(Week.SAT); newDays.add(Week.SUN);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("타이틀", "목표", days, "07:35", DAILY.getIndex());
        RoutineDTO.RequestRoutineDto mockRoutine = new RoutineDTO.RequestRoutineDto("타이틀 수정", "수정", newDays, "07:35", DAILY.getIndex());
        Routine fakeRoutine = Routine.builder().account(account).newRoutine(newRoutine).build();
        // mocking
        given(routineRepository.findByIdAndIsDeleteIsFalse(1L)).willReturn(Optional.of(fakeRoutine));
        given(routineRepository.save(any())).willReturn(fakeRoutine);

        // when
        RoutineDTO.ResponseRoutineDto routine = routineService.updateRoutine(1L, mockRoutine, account).getData();

        // then
        assertThat(routine.getTitle()).isEqualTo(mockRoutine.getTitle());
    }

    @Test
    void testUpdateRoutineSequenceSuccess() {
        //given
        Account account = AccountTemplate.makeTestAccount();
        List<Routine> routines = new ArrayList<>();
        List<Week> newDays1 = new ArrayList<>();
        List<Week> newDays2 = new ArrayList<>();
        ArrayList<Long> sequence = new ArrayList<>();
        sequence.add(2L); sequence.add(1L);
        newDays1.add(Week.MON); newDays2.add(Week.MON); newDays2.add(Week.SUN);
        RoutineDTO.RequestRoutineDto newRoutine1 = new RoutineDTO.RequestRoutineDto("타이틀", "목표", newDays1, "07:35", DAILY.getIndex()); // 1번
        RoutineDTO.RequestRoutineDto newRoutine2 = new RoutineDTO.RequestRoutineDto("타이틀2", "목표2", newDays2, "07:35", DAILY.getIndex()); // 2번
        routines.add(Routine.builder().account(account).newRoutine(newRoutine2).id(0L).build()); // 2번 -> 1번
        routines.add(Routine.builder().account(account).newRoutine(newRoutine1).id(1L).build()); // 1번 -> 2번

        // mocking
        given(routineRepository.findAllById(sequence)).willReturn(routines);
        given(routineRepository
                .findAllByIsDeleteIsFalseAndAccountAndDaysDayOrderByDaysSequence(account, Week.MON, Sort.by("days").descending())).willReturn(routines);

        // when
        List<RoutineDTO.ResponseRoutineDto> responseRoutineDtos = routineService.updateRoutineSequence(Week.MON, sequence, account).getData();

        // then
        assertThat(responseRoutineDtos.get(0).getTitle()).isEqualTo(newRoutine2.getTitle());
    }

    @Test
    void testGetDaysRoutineRateSuccessBothBeforeWeekCaseAndAfterWeekCase() {
        /* 월 수 금 루틴을 금요일에 생성한 Case + 루틴 생성 일주일 지난 일반 케이스*/
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Routine> routineList = new ArrayList<>();
        List<Retrospect> retrospectList = new ArrayList<>();
        Routine coffeeRoutine = RoutineTemplate.makeCoffeeRoutine(account);
        List<Retrospect> coffeeRetrospectList = RoutineTemplate.makeCoffeeRetrospectList(coffeeRoutine);
        Routine readingRoutine = RoutineTemplate.makeReadingRoutine(account);
        List<Retrospect> readingRetrospectList = RoutineTemplate.makeReadingRetrospectList(readingRoutine);
        Routine runningRoutine = RoutineTemplate.makeRunningRoutine(account);
        List<Retrospect> runningRetrospectList = RoutineTemplate.makeRunningRetrospectList(runningRoutine);
        Routine waterRoutine = RoutineTemplate.makeWaterRoutine(account);
        List<Retrospect> waterRetrospectList = RoutineTemplate.makeWaterRetrospectList(waterRoutine);
        Routine vitaminRoutine = RoutineTemplate.makeVitaminRoutine(account);

        routineList.add(coffeeRoutine);
        routineList.add(readingRoutine);
        routineList.add(runningRoutine);
        routineList.add(waterRoutine);
        routineList.add(vitaminRoutine);
        retrospectList.addAll(coffeeRetrospectList);
        retrospectList.addAll(readingRetrospectList);
        retrospectList.addAll(runningRetrospectList);
        retrospectList.addAll(waterRetrospectList);
        LocalDate start = LocalDate.parse("2021-10-18");

        // mocking
        given(retrospectRepository.findAllByDateBetweenAndRoutineAccount(
                start, start.plusDays(6), account)).willReturn(retrospectList);
        given(routineRepository.findAllByIsDeleteIsFalseAndAccount(account)).willReturn(routineList);
        // when
        List<RoutineDTO.ResponseRoutineDaysRate> result = routineService.getRoutineDaysRate(account, start).getData();

        // then
        /* 월 ~ 일 */
        assertAll(
                () -> assertThat(result.get(0).getRate()).isEqualTo(0),
                () -> assertThat(result.get(1).getRate()).isEqualTo(33),
                () -> assertThat(result.get(2).getRate()).isEqualTo(25),
                () -> assertThat(result.get(3).getRate()).isEqualTo(33),
                () -> assertThat(result.get(4).getRate()).isEqualTo(25),
                () -> assertThat(result.get(5).getRate()).isEqualTo(50),
                () -> assertThat(result.get(6).getRate()).isEqualTo(33)
        );
    }

    @Test
    void testGetDaysRoutineRateFailByNotMonDay() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        LocalDate start = LocalDate.parse("2021-10-19");

        // when then
        assertThrows(RoutineStartDayBadRequestException.class, () -> routineService.getRoutineDaysRate(account, start));
    }

    @Test
    void testGetRecommendedRoutine() {
        // given
        List<Organization> recommendedList = new ArrayList<>();
        Organization test1 = OrganizationTemplate.makeTestOrganization();
        Organization test2 = OrganizationTemplate.makeTestOrganization("환기 하기", Category.DAILY);
        recommendedList.add(test1); recommendedList.add(test2);

        // mocking
        given(groupService.findAll()).willReturn(recommendedList);

        // when
        List<RoutineDTO.ResponseRecommendedRoutine> data = routineService.getRecommendedRoutine().getData();

        // then
        assertAll(
                () -> assertEquals(data.get(0).getCategory(), RoutineCategory.MIRACLE.getIndex()),
                () -> assertEquals(data.get(1).getCategory(), RoutineCategory.DAILY.getIndex())
        );
   }

}
