package com.yapp.project.account.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.dto.AccountRequestDto;
import com.yapp.project.account.domain.dto.AccountResponseDto;
import com.yapp.project.account.domain.dto.TokenDto;
import com.yapp.project.account.domain.dto.TokenRequestDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.PrefixType;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.account.EmailDuplicateException;
import com.yapp.project.config.exception.account.NotFoundUserInformationException;
import com.yapp.project.config.exception.account.TokenInvalidException;
import com.yapp.project.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Message logout(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long accountId = Long.parseLong(authentication.getName());
        if (accountRepository.findById(accountId).isPresent()){
            redisTemplate.delete(PrefixType.PREFIX_REFRESH_TOKEN+authentication.getName());
        }
        return Message.of("로그아웃 되었습니다.");
    }

    @Transactional
    public AccountResponseDto signup(AccountRequestDto accountRequestDto){
        if (accountRepository.existsByEmail(accountRequestDto.getEmail())){
            throw new EmailDuplicateException("이미 가입되어 있는 유저입니다. ", StatusEnum.BAD_REQUEST);
        }
        Account account = accountRequestDto.toAccount(passwordEncoder);
        return AccountResponseDto.of(accountRepository.save(account));
    }

    @Transactional
    public TokenDto login(AccountRequestDto accountRequestDto){
        Account account = accountRepository.findByEmail(accountRequestDto.getEmail())
                .orElseThrow(() -> new NotFoundUserInformationException("알맞은 회원정보가 없습니다.",StatusEnum.NOT_FOUND));
        account.updateLastLoginAccount();
        UsernamePasswordAuthenticationToken authenticationToken = accountRequestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // refreshToken redis에 저장
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String key = PrefixType.PREFIX_REFRESH_TOKEN + authentication.getName();
        valueOperations.set(key, tokenDto.getRefreshToken());
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())){
            throw new TokenInvalidException("Refresh Token이 유효하지 않습니다.", StatusEnum.BAD_REQUEST);
        }
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getRefreshToken());
        String refreshToken = valueOperations.get(PrefixType.PREFIX_REFRESH_TOKEN + authentication.getName());
        if (refreshToken == null){
            throw new NotFoundUserInformationException("로그아웃된 사용자입니다.",StatusEnum.BAD_REQUEST);
        }

        if (!refreshToken.equals(tokenRequestDto.getRefreshToken())){
            throw new TokenInvalidException("토큰의 유저 정보가 일치하지 않습니다. ", StatusEnum.NOT_FOUND);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        // redis refreshToken update
        String key = PrefixType.PREFIX_REFRESH_TOKEN + authentication.getName();
        valueOperations.set(key, tokenDto.getRefreshToken());
        return tokenDto;
    }


}
