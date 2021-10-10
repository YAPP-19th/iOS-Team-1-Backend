package com.yapp.project.organization.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Double rate;

    private String image;

    private String recommend;

    private String shoot;

    private String promise;

    private String category;

    private Long count;



}
