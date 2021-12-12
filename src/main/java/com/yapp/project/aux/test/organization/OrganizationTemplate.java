package com.yapp.project.aux.test.organization;

import com.yapp.project.organization.domain.Category;
import com.yapp.project.organization.domain.Clause;
import com.yapp.project.organization.domain.Organization;

import java.time.LocalTime;

public class OrganizationTemplate {
    private OrganizationTemplate(){
    }
    private static Long id = 1L;
    public static final String TITLE = "명상";
    public static final Category CATEGORY = Category.MIRACLE;
    public static final LocalTime BEGIN_TIME = LocalTime.of(0,0);
    public static final LocalTime END_TIME = LocalTime.of(23,59, 59);
    public static final String DESCRIPTION = "고요히 자기 자신을 느껴보는 시간입니다.";
    public static final String RECOMMEND = "명상을 처음 접해본다면, 호흡부터 시작해보세요.";

    private static final String SHOOT = "명상을 할 조용한 장소";

    private static final Clause CLAUSE = Clause.builder().beginTime(BEGIN_TIME).endTime(END_TIME).shoot(SHOOT)
            .description(DESCRIPTION).recommend(RECOMMEND).build();

    public static Organization makeTestOrganization(){
        return makeTestOrganization(TITLE,CATEGORY);
    }

    public static Organization makeTestOrganization(String title){
        return makeTestOrganization(title,CATEGORY);
    }

    public static Organization makeTestOrganization(String title, Category category){
        Organization organization = makeTestOrganizationForIntegration(title,category);
        organization.setIdForTest(id++);
        return organization;
    }

    public static Organization makeTestOrganizationForIntegration(){
        return makeTestOrganizationForIntegration(TITLE,CATEGORY);
    }

    public static Organization makeTestOrganizationForIntegration(String title){
        return makeTestOrganizationForIntegration(title,CATEGORY);
    }

    public static Clause makeClauseForIntegration(LocalTime beginTime, LocalTime endTime) {
        return Clause.builder().beginTime(beginTime).endTime(endTime).shoot(SHOOT)
                .description(DESCRIPTION).recommend(RECOMMEND).build();
    }

    public static Organization makeTestOrganizationForIntegration(String title, Category category){
        return makeTestOrganizationForIntegration(title,category,CLAUSE);
    }

    public static Organization makeTestOrganizationForIntegration(String title, Category category,Clause clause){
        Organization organization = Organization.builder().title(title).category(category).clause(clause).rate(86).build();
        organization.defaultSettingForTest();
        return organization;
    }

}
