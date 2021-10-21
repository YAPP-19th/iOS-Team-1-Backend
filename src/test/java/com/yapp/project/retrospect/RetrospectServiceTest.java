package com.yapp.project.retrospect;

import com.yapp.project.account.domain.Account;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.retrospect.domain.Result;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.retrospect.domain.RetrospectRepository;
import com.yapp.project.retrospect.domain.dto.RetrospectDTO;
import com.yapp.project.retrospect.service.RetrospectService;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.RoutineDTO;
import com.yapp.project.routine.domain.Week;
import com.yapp.project.routine.service.RoutineService;
import com.yapp.project.snapshot.domain.Snapshot;
import com.yapp.project.snapshot.domain.SnapshotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RetrospectServiceTest {

    @Mock
    private RoutineService routineService;
    @InjectMocks
    private RetrospectService retrospectService;
    @Mock
    private RetrospectRepository retrospectRepository;
    @Mock
    private SnapshotRepository snapshotRepository;

    @Test
    void Test_Create_Retrospect_Success() {
        // given
        Account account = AccountTemplate.makeTestAccount();
        List<Week> days = new ArrayList<>();
        days.add(Week.MON);
        days.add(Week.TUE);
        RoutineDTO.RequestRoutineDto newRoutine = new RoutineDTO.RequestRoutineDto("타이틀", "목포", days, "07:35", "생활");
        Routine fakeRoutine = Routine.builder().account(account).newRoutine(newRoutine).id(1L).build();
        Retrospect fakeRetrospect = Retrospect.builder().routine(fakeRoutine).isReport(false).result(Result.NOT).build();
        Snapshot fakeSnapShot = Snapshot.builder().url("테스트 경로").build();
        Retrospect fakeRetrospectSnapShot = Retrospect.builder().routine(fakeRoutine).isReport(false).result(Result.NOT).build();
        fakeRetrospectSnapShot.updateRetrospect("테스트 회고 내용", fakeSnapShot);
        RetrospectDTO.RequestRetrospect fakeRequestRetrospect = RetrospectDTO.RequestRetrospect.builder().routineId(1L).content("테스트 회고 내용").build();

        // mocking
        given(retrospectRepository.findByRoutineAndDate(fakeRoutine, LocalDate.now())).willReturn(Optional.of(fakeRetrospect));
        given(snapshotRepository.save(any())).willReturn(fakeSnapShot);
        given(retrospectRepository.save(any())).willReturn(fakeRetrospectSnapShot);
        given(routineService.findIsExistByIdAndIsNotDelete(1L)).willReturn(fakeRoutine);

        // when
        RetrospectDTO.ResponseRetrospect fakeResponseRetrospect = retrospectService.createRetrospect(fakeRequestRetrospect, "테스트 경로", account).getData();

        assertThat(fakeRequestRetrospect.getContent()).isEqualTo(fakeResponseRetrospect.getContent());
        assertThat(fakeRequestRetrospect.getRoutineId()).isEqualTo(fakeResponseRetrospect.getRoutine().getId());
    }

    @Test
    void Test_Save_And_Get_Path_Success() {
        // Todo 이미지 저장 테스트 케이스는 S3 적용 후 작성
    }
}
