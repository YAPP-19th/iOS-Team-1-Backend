package com.yapp.project.account.domain.dto;

import com.yapp.project.account.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {
    private String email;

    public static AccountResponseDto of(Account account) {
        return new AccountResponseDto(account.getEmail());
    }
}
