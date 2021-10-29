package com.yapp.project.aux.common;

import com.yapp.project.account.domain.Account;
import com.yapp.project.config.security.PrincipalDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AccountUtil {
    private AccountUtil(){
    }
    public static Account getAccount() {
        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getAccount();
    }

    public static String generateMD5(String email) throws NoSuchAlgorithmException {
        StringBuilder stringBuffer = new StringBuilder();

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(email.getBytes());

        byte[] msgStr = messageDigest.digest();

        for (byte b : msgStr) {
            String tmpEncTxt = Integer.toHexString(b & 0x00FF);
            stringBuffer.append(tmpEncTxt);
        }
        return stringBuffer.toString();
    }
}
