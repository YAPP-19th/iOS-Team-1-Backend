package com.yapp.project.routine;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.domain.RoutineRepository;
import com.yapp.project.routine.domain.Week;
import com.yapp.project.routine.service.RoutineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindException;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
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
        try {
            RoutineDTO.ResponseRoutineDto routine = routineService.createRoutine(newRoutine, account);
            // then
            assertThat(routine.getTitle()).isEqualTo(newRoutine.getTitle());
            assertThat(routine.getGoal()).isEqualTo(newRoutine.getGoal());
            assertThat(routine.getStartTime()).isEqualTo(newRoutine.getStartTime());
            assertThat(routine.getCategory()).isEqualTo(newRoutine.getCategory());
        } catch (BindException e) {
            e.printStackTrace();
        }
    }

    @Test
    void Test_Create_Routine_Failure() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("", "", days, "07:35", "생활");
        // when
        try {
            routineService.createRoutine(newRoutine, account);
        } catch (BindException e) {
            // then
            assertThat(e).isNotNull();
        }
    }
}
