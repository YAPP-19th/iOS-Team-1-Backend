package com.yapp.project.routine;

import com.yapp.project.account.domain.dto.TokenDto;
import com.yapp.project.account.service.AuthService;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.config.jwt.TokenProvider;
import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.domain.Week;
import com.yapp.project.routine.service.RoutineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RoutineServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private RoutineService routineService;
    @Autowired
    private TokenProvider tokenProvider;

    @BeforeEach
    public void setup() {
        authService.signup(AccountTemplate.makeAccountRequestDto());
        TokenDto tokenDto = authService.login(AccountTemplate.makeAccountRequestDto());
        Authentication authentication = tokenProvider.getAuthentication(tokenDto.getAccessToken());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @Transactional
    void Test_Create_Routine_Success() {
        // given
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", "생활");
        // when
        try {
            RoutineDTO.ResponseRoutineDto routine = routineService.createRoutine(newRoutine);
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
    @Transactional
    void Test_Create_Routine_Failure() {
        // given
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("", "목포", days, "07:35", "생활");
        // when
        try {
            routineService.createRoutine(newRoutine);
        } catch (BindException e) {
            // then
            assertThat(e).isNotNull();
        }
    }
}
