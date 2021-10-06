package com.yapp.project.account.service;
import com.yapp.project.account.domain.*;
import com.yapp.project.account.domain.dto.*;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.PrefixType;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.Content;
import com.yapp.project.config.exception.account.DuplicateException;
import com.yapp.project.config.exception.account.NotFoundUserInformationException;
import com.yapp.project.config.exception.account.TokenInvalidException;
import com.yapp.project.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${social.value}")
    private String suffix;

    @Transactional
    public Message socialAccess(SocialDto.Request requestDto){
        String emailSuffix = "";
        SocialType socialType = null;
        if (requestDto.getSocialType().equalsIgnoreCase(SocialType.KAKAO.toString())){
            emailSuffix = "@kakao.com";
            socialType = SocialType.KAKAO;
        }else{
            emailSuffix = "@apple.com";
            socialType = SocialType.APPLE;
        }
        if (accountRepository.existsByEmail(requestDto.getId()+emailSuffix)){
            Account account = accountRepository.findByEmail(requestDto.getId()+emailSuffix).orElse(null);
            assert  account!=null;
            TokenDto tokenDto = login(account.toAccountRequestDto(suffix));
            SocialDto.Response responseDto = new SocialDto.Response("LOGIN",tokenDto);
            return Message.builder().status(StatusEnum.OK).msg("소셜 로그인 성공").data(responseDto).build();
        }else {
            Account account = Account.builder().email(requestDto.getId()+emailSuffix).socialType(socialType)
                    .build();
            SocialDto.Response responseDto = new SocialDto.Response("SIGNUP",account);
            return Message.builder().status(StatusEnum.OK).msg("소셜 회원가입 진행중").data(responseDto).build();
        }
    }

    @Transactional
    public Message socialSignUp(SocialDto.SignUpRequest requestDto){
        if (accountRepository.existsByEmail(requestDto.getEmail())){
            throw new DuplicateException(Content.EMAIL_DUPLICATE, StatusEnum.BAD_REQUEST);
        }
        if (accountRepository.existsByNickname(requestDto.getNickname())){
            throw new DuplicateException(Content.NICKNAME_DUPLICATE,StatusEnum.BAD_REQUEST);
        }
        Account account = requestDto.toAccount(passwordEncoder,suffix);
        accountRepository.save(account);
        TokenDto tokenDto = login(account.toAccountRequestDto(suffix));
        return Message.builder().msg("소셜 회원가입").status(StatusEnum.OK).data(tokenDto).build();
    }


    @Transactional
    public Message logout(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountEmail = authentication.getName();
        if (accountRepository.findByEmail(accountEmail).isPresent()){
            redisTemplate.delete(PrefixType.PREFIX_REFRESH_TOKEN+authentication.getName());
        }else{
            throw new NotFoundUserInformationException(Content.NOT_FOUND_USER_INFORMATION,StatusEnum.BAD_REQUEST);
        }
        return Message.of("로그아웃 되었습니다.");
    }

    @Transactional
    public AccountDto.Response signup(AccountDto.Request accountRequestDto){
        if (accountRepository.existsByEmail(accountRequestDto.getEmail())){
            throw new DuplicateException(Content.EMAIL_DUPLICATE, StatusEnum.BAD_REQUEST);
        }
        if (accountRepository.existsByNickname(accountRequestDto.getNickname())){
            throw new DuplicateException(Content.NICKNAME_DUPLICATE,StatusEnum.BAD_REQUEST);
        }
        Account account = accountRequestDto.toAccount(passwordEncoder);
        return AccountDto.Response.of(accountRepository.save(account));
    }

    @Transactional
    public TokenDto login(AccountDto.Request accountRequestDto){
        Account account = accountRepository.findByEmail(accountRequestDto.getEmail())
                .orElseThrow(() -> new NotFoundUserInformationException(Content.NOT_FOUND_USER_INFORMATION,StatusEnum.NOT_FOUND));
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
            throw new TokenInvalidException(Content.REFRESH_TOKEN_INVALID, StatusEnum.BAD_REQUEST);
        }
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getRefreshToken());
        String refreshToken = valueOperations.get(PrefixType.PREFIX_REFRESH_TOKEN + authentication.getName());
        if (refreshToken == null){
            throw new NotFoundUserInformationException(Content.LOGOUT_USER,StatusEnum.BAD_REQUEST);
        }

        if (!refreshToken.equals(tokenRequestDto.getRefreshToken())){
            throw new TokenInvalidException(Content.TOKEN_NOT_EQUAL_USER_INFORMATION, StatusEnum.NOT_FOUND);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        // redis refreshToken update
        String key = PrefixType.PREFIX_REFRESH_TOKEN + authentication.getName();
        valueOperations.set(key, tokenDto.getRefreshToken());
        return tokenDto;
    }


}