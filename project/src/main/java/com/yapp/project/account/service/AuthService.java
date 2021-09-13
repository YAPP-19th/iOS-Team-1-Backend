package com.yapp.project.account.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.dto.AccountRequestDto;
import com.yapp.project.account.domain.dto.AccountResponseDto;
import com.yapp.project.account.domain.dto.TokenDto;
import com.yapp.project.account.domain.dto.TokenRequestDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String,String> redisTemplate;


    @Transactional
    public AccountResponseDto signup(AccountRequestDto accountRequestDto){
        if (accountRepository.existsByEmail(accountRequestDto.getEmail())){
            throw new IllegalArgumentException("이미 가입되어 있는 유저입니다. ");
        }
        Account account = accountRequestDto.toAccount(passwordEncoder);
        return AccountResponseDto.of(accountRepository.save(account));
    }

    @Transactional
    public TokenDto login(AccountRequestDto accountRequestDto){
        UsernamePasswordAuthenticationToken authenticationToken = accountRequestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(authentication.getName(),tokenDto.getRefreshToken());
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())){
            throw new IllegalArgumentException("Refresh Token이 유효하지 않습니다.");
        }
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getRefreshToken());
        String refreshToken = valueOperations.get(authentication.getName());
        if (refreshToken == null){
            throw new IllegalArgumentException("로그아웃된 사용자입니다.");
        }

        if (!refreshToken.equals(tokenRequestDto.getRefreshToken())){
            throw new IllegalArgumentException("토큰의 유저 정보가 일치하지 않습니다. ");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        valueOperations.set(authentication.getName(),tokenDto.getRefreshToken());

        return tokenDto;
    }


}
