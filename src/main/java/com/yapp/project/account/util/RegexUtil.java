package com.yapp.project.account.util;

import java.util.regex.Pattern;

public class RegexUtil {
    private RegexUtil(){
    }
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static boolean validPassword(String password){
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    public static boolean validEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
