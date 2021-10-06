package com.yapp.project.organization.domain;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organization {

    @Builder
    public Organization(String title, Theme theme, String image, String recommend, String capture, String promise, Double rate){
        Assert.hasText(title,"제목은 반드시 있어야 합니다.");
        this.title = title;
        this.theme = theme;
        this.image = image;
        this.recommend = recommend;
        this.capture = capture;
        this.promise = promise;
        this.rate = rate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "organization",cascade = CascadeType.ALL)
    private final List<Participant> participants = new ArrayList<>();

    private String title;

    @Enumerated(EnumType.STRING)
    private Theme theme;

    private String image;

    private String recommend;

    private String capture;

    private String promise;

    private Double rate;

}
