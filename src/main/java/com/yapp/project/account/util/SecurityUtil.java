package com.yapp.project.account.util;

import com.yapp.project.config.exception.account.NotFoundUserInformationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {
    private SecurityUtil(){
    }

    public static String getCurrentAccountEmail() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() ==null){
            throw new NotFoundUserInformationException();
        }
        return authentication.getName();
    }
}
