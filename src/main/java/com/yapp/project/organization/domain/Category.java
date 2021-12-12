package com.yapp.project.organization.domain;

import lombok.Getter;

@Getter
public enum Category {
    MIRACLE("MIRACLE", 0),
    SELF( "SELF", 1),
    HEALTH("HEALTH", 2),
    DAILY( "DAILY", 3),
    MORNING( "MORNING", 4),
    ETC("ETC",5);

    private static final int SIZE = 6;
    private final String name;
    private final Integer index;

    Category(String name, Integer index) {
        this.name = name;
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
                "name='" + name + '\'' +
                ", index=" + index +
                '}';
    }
}
