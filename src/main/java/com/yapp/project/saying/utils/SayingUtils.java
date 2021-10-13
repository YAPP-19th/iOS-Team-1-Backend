package com.yapp.project.saying.utils;

import java.util.Random;

public class SayingUtils {
    private SayingUtils(){
    }
    public static final int SIZE = 4;
    private static Random random = new Random();
    public static int randomSayingId(){
        return 1 + random.nextInt(SIZE);
    }

}
