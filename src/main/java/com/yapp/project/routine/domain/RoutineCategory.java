package com.yapp.project.routine.domain;

import lombok.Getter;

@Getter
public enum RoutineCategory {
    MIRACLE("MIRACLE", 0),
    SELF( "SELF", 1),
    HEALTH("HEALTH", 2),
    DAILY( "DAILY", 3),
    ETC("ETC",4);

    private static final int SIZE = 5;
    private final String name;
    private final Integer index;

    RoutineCategory(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public static int size() {
        return SIZE;
    }

    public static RoutineCategory indexToCategory(Integer index) {
        RoutineCategory category;
        switch (index){
            case 0:
                category = RoutineCategory.MIRACLE;
                break;
            case 1:
                category = RoutineCategory.SELF;
                break;
            case 2:
                category = RoutineCategory.HEALTH;
                break;
            case 3:
                category = RoutineCategory.DAILY;
                break;
            default:
                category = RoutineCategory.ETC;
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
