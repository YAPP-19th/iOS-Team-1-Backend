package com.yapp.project.aux.common;

import java.util.Random;

public class Utils {
    private Utils(){
    }
    public static final int SAYING_SIZE = 4;
    public static final int SIZE = 9;
    private static final Random RANDOM = new Random();
    public static int randomSayingId(){
        return 1 + RANDOM.nextInt(SAYING_SIZE);
    }
    public static int randomNumber(){
        return 1 + RANDOM.nextInt(SIZE);
    }
    public static String authenticationNumber(){
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            number.append(randomNumber());
        }
        return number.toString();
    }

}
