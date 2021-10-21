package com.yapp.project.saying.utils;

import java.util.Random;

public class SayingUtils {
    private SayingUtils(){
    }
    public static final int SIZE = 4;
    private static final Random RANDOM = new Random();
    public static int randomSayingId(){
        return 1 + RANDOM.nextInt(SIZE);
    }

}
