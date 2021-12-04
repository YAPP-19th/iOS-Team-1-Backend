package com.yapp.project.routine.domain;

import com.yapp.project.account.domain.Account;
import com.yapp.project.retrospect.domain.Retrospect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.yapp.project.aux.common.DateUtil.KST_LOCAL_DATETIME_NOW;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private Boolean isDelete;

    @Column(nullable = false)
    private String category;

    @OneToMany(mappedBy = "routine", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoutineDay> days = new ArrayList<>();

    @OneToMany(mappedBy = "routine")
    private List<Retrospect> retrospects = new ArrayList<>();

    @Builder
    public Routine(Account account, RoutineDTO.RequestRoutineDto newRoutine, Long id){
        this.account = account;
        this.title = newRoutine.getTitle();
        this.goal = newRoutine.getGoal();
        this.startTime = LocalTime.parse(newRoutine.getStartTime());
        this.isDelete = false;
        this.category = newRoutine.getCategory();
        this.createdAt = KST_LOCAL_DATETIME_NOW();
        this.id = id;
    }

    public void addDays(List<RoutineDay> days) {
        this.days.addAll(days);
    }

    public void updateRoutine(RoutineDTO.RequestRoutineDto updateRoutine) {
        this.title = updateRoutine.getTitle();
        this.goal = updateRoutine.getGoal();
        this.startTime = LocalTime.parse(updateRoutine.getStartTime());
        this.isDelete = false;
        this.category = updateRoutine.getCategory();
    }

    public void deleteRoutine() {
        this.account = null;
        this.isDelete = true;
    }

    /** Test */
    public void updateCreateAt(LocalDateTime localDateTime) {
        this.createdAt = localDateTime;
    }
}