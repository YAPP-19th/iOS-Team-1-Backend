package com.yapp.project.base;

import com.yapp.project.mission.domain.Mission;
import com.yapp.project.routine.domain.Routine;
import com.yapp.project.routine.domain.Week;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Week week;

    @ManyToOne
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;





    private LocalDateTime createdAt;
}
