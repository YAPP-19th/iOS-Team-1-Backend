package com.yapp.project.aux.test.organization;

import com.yapp.project.organization.domain.Clause;
import com.yapp.project.organization.domain.Organization;

public class OrganizationTemplate {
    private OrganizationTemplate(){
    }
    private static Long id = 1L;
    public static final String TITLE = "명상";
    public static final String CATEGORY = "미라클모닝";
    public static final Integer START_TIME = 5;
    public static final Integer FINISH_TIME = 8;

    private static final String SHOOT = "명상을 할 조용한 장소";

    private static final Clause CLAUSE = Clause.builder().beginTime(START_TIME).endTime(FINISH_TIME).shoot(SHOOT).build();

    public static Organization makeTestOrganization(){
        return makeTestOrganization(TITLE,CATEGORY);
    }

    public static Organization makeTestOrganization(String title){
        return makeTestOrganization(title,CATEGORY);
    }

    public static Organization makeTestOrganization(String title, String category){
        Organization organization = Organization.builder().id(id++).title(title).category(category).clause(CLAUSE).rate(86).build();
        organization.defaultSetting();
        return organization;
    }

}
