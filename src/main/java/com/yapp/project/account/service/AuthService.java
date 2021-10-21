package com.yapp.project.account.service;
import com.yapp.project.account.domain.*;
import com.yapp.project.account.domain.dto.AccountDto.*;
import com.yapp.project.account.domain.dto.SocialDto.*;
import com.yapp.project.account.domain.dto.TokenDto;
import com.yapp.project.account.domain.dto.TokenRequestDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.account.util.PasswordUtil;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.PrefixType;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.config.exception.account.*;
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

    @Transactional(readOnly = true)
    public SocialResponseMessage socialAccess(SocialRequest socialRequestDto){
        SocialType socialType = socialRequestDto.getSocial();
        String email = socialRequestDto.getEmail();
        Account account = accountRepository.findByEmail(email).orElse(null);
        if (account != null){
            TokenDto tokenDto = login(account.toAccountRequestDto(suffix));
            SocialResponse socialResponseDto = new SocialResponse("LOGIN",tokenDto);
            return socialResponseDto.toSocialResponseMessage("소셜 로그인 성공");
        }else {
            MiddleAccount middleAccount = MiddleAccount.builder().email(email).socialType(socialType)
                    .build();
            SocialResponse socialResponseDto = new SocialResponse("SIGNUP", middleAccount);
            return socialResponseDto.toSocialResponseMessage("소셜 회원가입 진행중");
        }
    }


    @Transactional
    public TokenMessage socialSignUp(SocialSignUpRequest requestDto){
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();
        checkDuplicateExceptionFields(email, nickname);
        Account account = requestDto.toAccount(passwordEncoder,suffix);
        accountRepository.save(account);
        TokenDto tokenDto = login(account.toAccountRequestDto(suffix));
        return tokenDto.toTokenMessage("소셜 회원가입", StatusEnum.SOCIAL_OK);

    }


    @Transactional(readOnly = true)
    public TokenMessage normalLogin(UserRequest accountRequestDto){
        return login(accountRequestDto).toTokenMessage("기본 로그인", StatusEnum.ACCOUNT_OK);
    }


    @Transactional
    public TokenMessage normalSignUp(UserRequest accountRequestDto){
        accountRequestDto.updateSocialType(SocialType.NORMAL);
        signUp(accountRequestDto);
        return login(accountRequestDto).toTokenMessage("회원가입 축하드립니다", StatusEnum.ACCOUNT_OK);
    }


    @Transactional(readOnly = true)
    public Message logout(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountEmail = authentication.getName();
        if (accountRepository.findByEmail(accountEmail).isPresent()){
            redisTemplate.delete(PrefixType.PREFIX_REFRESH_TOKEN+authentication.getName());
        }else{
            throw new NotFoundUserInformationException();
        }
        return Message.of(StatusEnum.ACCOUNT_OK,"로그아웃 되었습니다.");
    }


    @Transactional(readOnly = true)
    public Message existByNickname(String nickname){
        if (accountRepository.existsByNickname(nickname))
            throw new NicknameDuplicateException();
        return Message.of("중복되는 닉네임이 없습니다.");
    }


    @Transactional
    public TokenMessage reissue(TokenRequestDto tokenRequestDto){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())){
            throw new RefreshTokenInvalidException();
        }
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getRefreshToken());
        String refreshToken = valueOperations.get(PrefixType.PREFIX_REFRESH_TOKEN + authentication.getName());

        reissueRefreshExceptionCheck(refreshToken, tokenRequestDto);

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        // redis refreshToken update
        String key = PrefixType.PREFIX_REFRESH_TOKEN + authentication.getName();
        valueOperations.set(key, tokenDto.getRefreshToken());
        return tokenDto.toTokenMessage("토큰 재발급", StatusEnum.TOKEN_OK);
    }


    public UserResponse signUp(UserRequest accountRequestDto){
        String email = accountRequestDto.getEmail();
        String nickname = accountRequestDto.getNickname();
        checkDuplicateExceptionFields(email, nickname);
        if (accountRequestDto.getSocialType().equals(SocialType.NORMAL) && !PasswordUtil.validPassword(accountRequestDto.getPassword())){
                throw new PasswordInvalidException();
        }
        Account account = accountRequestDto.toAccount(passwordEncoder);
        return UserResponse.of(accountRepository.save(account));
    }

    private void checkDuplicateExceptionFields(String email, String nickname){
         if (accountRepository.existsByEmail(email)){
            throw new EmailDuplicateException();
        }
        if (accountRepository.existsByNickname(nickname)){
            throw new NicknameDuplicateException();
        }
    }


    public TokenDto login(UserRequest accountRequestDto){
        Account account = accountRepository.findByEmail(accountRequestDto.getEmail())
                .orElseThrow(NotFoundUserInformationException::new);
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


    private void reissueRefreshExceptionCheck(String refreshToken,TokenRequestDto tokenRequestDto){
        if (refreshToken == null){
            throw new AlreadyLogoutException();
        }
        if (!refreshToken.equals(tokenRequestDto.getRefreshToken())){
            throw new TokenInvalidException();
        }
    }

}