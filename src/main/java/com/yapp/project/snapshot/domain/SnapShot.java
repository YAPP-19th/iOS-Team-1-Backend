package com.yapp.project.snapshot.domain;

import com.yapp.project.organization.domain.Organization;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnapShot {

    @Builder
    public SnapShot(Organization organization, String picture){
        this.organization = organization;
        this.createdAt = LocalDateTime.now();
        this.picture = picture;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    // retrospect

    private LocalDateTime createdAt;

    private String picture;
}
