package com.yapp.project.snapshot.domain;

import com.yapp.project.organization.domain.Organization;
import com.yapp.project.retrospect.domain.Retrospect;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Snapshot {

    @Builder
    public Snapshot(Organization organization, String picture){
        this.organization = organization;
        this.createdAt = LocalDateTime.now();
        this.url = url;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    // retrospect
    @OneToOne
    @JoinColumn(name = "retrospect_id")
    private Retrospect retrospect;

    private LocalDateTime createdAt;

    private String url;
}
