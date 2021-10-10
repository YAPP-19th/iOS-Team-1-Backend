package com.yapp.project.routine.domain;

import com.yapp.project.account.domain.Account;
import com.yapp.project.retrospect.domain.Retrospect;
import com.yapp.project.routine.domain.dto.RequestRoutineDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "routine", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<RoutineDay> days = new ArrayList<>();

    @OneToMany(mappedBy = "routine")
    private List<Retrospect> retrospects = new ArrayList<>();

    @Builder
    public Routine(Account account, RequestRoutineDto newRoutine){
        this.account = account;
        this.title = newRoutine.getTitle();
        this.goal = newRoutine.getGoal();
        this.startTime = LocalTime.parse(newRoutine.getStartTime());
        this.isDelete = false;
        this.category = newRoutine.getCategory();
        this.createdAt = LocalDateTime.now();
    }

    public void addDays(RoutineDay day) {
        this.days.add(day);
        day.setRoutine(this);
    }
}
