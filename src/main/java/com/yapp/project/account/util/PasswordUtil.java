package com.yapp.project.account.util;

public class PasswordUtil {
    private PasswordUtil(){
    }
    public static boolean validPassword(String password){
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(pattern);
    }
}
