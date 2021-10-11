package com.yapp.project.routine;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.domain.RoutineRepository;
import com.yapp.project.routine.domain.Week;
import com.yapp.project.routine.exception.BadRequestException;
import com.yapp.project.routine.exception.NotFoundRoutineException;
import com.yapp.project.routine.service.RoutineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RoutineServiceTest {

    @InjectMocks
    private RoutineService routineService;

    @Mock
    private RoutineRepository routineRepository;

    @Test
    void Test_Create_Routine_Success() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", "생활");
        Routine fakeRoutine = Routine.builder().account(account).newRoutine(newRoutine).build();

        // mocking
        given(routineRepository.save(any())).willReturn(fakeRoutine);
        // when
        RoutineDTO.ResponseRoutineDto routine = routineService.createRoutine(newRoutine, account);
        // then
        assertThat(routine.getTitle()).isEqualTo(newRoutine.getTitle());
        assertThat(routine.getGoal()).isEqualTo(newRoutine.getGoal());
        assertThat(routine.getStartTime()).isEqualTo(newRoutine.getStartTime());
        assertThat(routine.getCategory()).isEqualTo(newRoutine.getCategory());
    }

    @Test
    void Test_Create_Routine_Failure_BadRequest() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("", "", days, "07:35", "생활");

        // when then
        assertThrows(BadRequestException.class, () -> {
            routineService.createRoutine(newRoutine, account);
        });
    }

    @Test
    void Test_Get_Routine_Success() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", "생활");
        Routine fakeRoutine = Routine.builder().account(account).newRoutine(newRoutine).build();

        // mocking
        given(routineRepository.findById(1L)).willReturn(Optional.of(fakeRoutine));

        // when
        RoutineDTO.ResponseRoutineDto routine = routineService.getRoutine(1L, account);
        // then
        assertThat(routine.getTitle()).isEqualTo(newRoutine.getTitle());
        assertThat(routine.getGoal()).isEqualTo(newRoutine.getGoal());
        assertThat(routine.getStartTime()).isEqualTo(newRoutine.getStartTime());
        assertThat(routine.getCategory()).isEqualTo(newRoutine.getCategory());
    }

    @Test
    void Test_Get_Routine_Failure_NotFound() {
        // given
        Account account = AccountTemplate.makeTestAccount();

        // when then
        assertThrows(NotFoundRoutineException.class, () -> {
            routineService.getRoutine(1L, account);
        });
    }

    @Test
    void Test_Get_Routine_Failure_BadRequest() {
        // given
        Account account1 = AccountTemplate.makeTestAccount();
        Account account2 = AccountTemplate.makeTestAccount("나는@두번째.사람");
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", "생활");
        Routine fakeRoutine = Routine.builder().account(account1).newRoutine(newRoutine).build();

        // mocking
        given(routineRepository.findById(1L)).willReturn(Optional.of(fakeRoutine));

        // when then
        assertThrows(BadRequestException.class, () -> {
            routineService.getRoutine(1L, account2);
        });
    }

    @Test
    void Test_Get_RoutineList_Success() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();
        List<Routine> routines = new ArrayList<>();
        days.add(Week.MON); days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine1 = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", "생활");
        RoutineDTO.RequestRoutineDto newRoutine2 = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", "생활");
        routines.add(Routine.builder().account(account).newRoutine(newRoutine1).build());
        routines.add(Routine.builder().account(account).newRoutine(newRoutine2).build());

        // mocking
        given(routineRepository
                .findAllByAccountAndDaysDayOrderByDaysSequence(account, Week.MON, Sort.by("days").descending())).willReturn(routines);
        // when
        List<RoutineDTO.ResponseRoutineDto> routineList = routineService.getRoutineList(Week.MON, account);
        // then
        assertThat(routineList.size()).isEqualTo(routines.size());
    }

}
