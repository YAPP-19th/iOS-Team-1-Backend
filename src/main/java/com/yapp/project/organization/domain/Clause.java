package com.yapp.project.organization.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Clause {
    private String recommend;

    private String shoot;

    private String promise;

    private String summary;

}
