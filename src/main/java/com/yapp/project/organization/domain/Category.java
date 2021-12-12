package com.yapp.project.organization.domain;

import lombok.Getter;

@Getter
public enum Category {
    MIRACLE("미라클모닝", "MIRACLE", 0),
    SELF("자기개발", "SELF", 1),
    HEALTH("건강","HEALTH", 2),
    DAILY("생활", "DAILY", 3),
    MORNING("기상", "MORNING", 4),
    ETC("기타","ETC",5);

    private static final int SIZE = 6;
    private final String korCategory;
    private final String engCategory;
    private final Integer index;

    Category(String korCategory, String engCategory, Integer index) {
        this.korCategory = korCategory;
        this.engCategory = engCategory;
        this.index = index;
    }

    public static int size() {
        return SIZE;
    }

    public static Category indexToCategory(Integer index) {
        Category category;
        switch (index){
            case 0:
                category = Category.MIRACLE;
                break;
            case 1:
                category = Category.SELF;
                break;
            case 2:
                category = Category.HEALTH;
                break;
            case 3:
                category = Category.DAILY;
                break;
            case 4:
                category = Category.MORNING;
                break;
            default:
                category = Category.ETC;
        }
        return category;
    }

    @Override
    public String toString() {
        return "Category{" +
                "korCategory='" + korCategory + '\'' +
                ", engCategory='" + engCategory + '\'' +
                ", index=" + index +
                '}';
    }
}
