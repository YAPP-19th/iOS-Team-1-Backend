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
    private String nickname;
    private String profile;

    public static AccountResponseDto of(Account account) {
        return new AccountResponseDto(account.getEmail(), account.getNickname(), account.getProfile());
    }
}
