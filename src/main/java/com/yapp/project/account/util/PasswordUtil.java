package com.yapp.project.account.util;

import java.util.regex.Pattern;

public class PasswordUtil {
    private PasswordUtil(){
    }
    private static final Pattern PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$");
    public static boolean validPassword(String password){
        return PATTERN.matcher(password).matches();
    }
}
