package com.yapp.project.aux.common;

import com.yapp.project.account.domain.Account;
import com.yapp.project.config.security.PrincipalDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class AccountUtil {
    public static Account getAccount() {
        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getAccount();
    }
}
