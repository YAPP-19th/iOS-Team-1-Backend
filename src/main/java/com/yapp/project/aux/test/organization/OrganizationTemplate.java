package com.yapp.project.aux.test.organization;

import com.yapp.project.organization.domain.Clause;
import com.yapp.project.organization.domain.Organization;

public class OrganizationTemplate {
    private OrganizationTemplate(){
    }
    private static Long id = 1L;
    public static final String TITLE = "명상";
    public static final String CATEGORY = "미라클모닝";
    public static final String SUMMARY = "오늘도 나만의 중심을 단단하게 다져보세요.";
    private static final String RECOMMEND = "명상을 처음 접해본다면, 호흡부터 시작해보세요.  \n" +
            "기도를 통해 들어가 발끝까지 뻗어나가는 호흡을 조용히 느껴보며 머리의 생각을 비워보세요.  \n" +
            "집중이 힘들다면 조용한 음악이나 자연의 소리를 틀어보는 것도 나쁘지 않아요. \n" +
            "소리에 귀를 기울이며 명상에 몰입해보세요.";

    private static final String SHOOT = "명상을 할 조용한 장소";

    private static final String PROMISE = "오전 5시 ~ 8시 사이 사진 업로드";

    private static final Clause CLAUSE = Clause.builder().recommend(RECOMMEND).promise(PROMISE).summary(SUMMARY)
            .shoot(SHOOT).build();

    public static Organization makeTestOrganization(){
        return makeTestOrganization(TITLE,CATEGORY);
    }

    public static Organization makeTestOrganization(String title){
        return makeTestOrganization(title,CATEGORY);
    }

    public static Organization makeTestOrganization(String title, String category){
        return Organization.builder().id(id++).title(title).category(category).clause(CLAUSE).rate(86.4).build();
    }

}
