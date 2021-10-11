package com.yapp.project.aux.test.organization;

import com.yapp.project.organization.domain.Organization;

public class OrganizationTemplate {
    private OrganizationTemplate(){
    }
    private static Long id = 1L;
    public static final String TITLE = "명상";
    public static final String CATEGORY = "미라클모닝";
    public static final String SUMMARY = "오늘도 나만의 중심을 단단하게 다져보세요.";

    public static Organization makeTestOrganization(){
        return Organization.builder().id(id++).title(TITLE).category(CATEGORY).summary(SUMMARY).build();
    }

    public static Organization makeTestOrganization(String title){
        return Organization.builder().id(id++).title(title).category(CATEGORY).summary(SUMMARY).build();
    }

    public static Organization makeTestOrganization(String title, String category){
        return Organization.builder().id(id++).title(title).category(category).summary(SUMMARY).build();
    }

    public static Organization makeTestOrganization(String title, String category, String summary){
        return Organization.builder().id(id++).title(title).category(category).summary(summary).build();
    }



}
