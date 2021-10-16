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
import com.yapp.project.config.exception.Content;
import com.yapp.project.config.exception.account.DuplicateException;
import com.yapp.project.config.exception.account.NotFoundUserInformationException;
import com.yapp.project.config.exception.account.NotValidateException;
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
    public SocialResponseMessage socialAccess(SocialRequest socialRequestDto){
        String emailSuffix = "";
        SocialType socialType = null;
        if (socialRequestDto.getSocialType().equalsIgnoreCase(SocialType.KAKAO.toString())){
            emailSuffix = "@kakao.com";
            socialType = SocialType.KAKAO;
        }else{
            emailSuffix = "@apple.com";
            socialType = SocialType.APPLE;
        }
        if (accountRepository.existsByEmail(socialRequestDto.getId()+emailSuffix)){
            Account account = accountRepository.findByEmail(socialRequestDto.getId()+emailSuffix).orElse(null);
            assert  account!=null;
            TokenDto tokenDto = login(account.toAccountRequestDto(suffix));
            SocialResponse socialResponseDto = new SocialResponse("LOGIN",tokenDto);
            return SocialResponseMessage.builder()
                    .message(
                            Message.builder()
                                    .status(StatusEnum.OK)
                                    .msg("소셜 로그인 성공")
                                    .build()
                    ).data(socialResponseDto).build();
        }else {
            Account account = Account.builder().email(socialRequestDto.getId()+emailSuffix).socialType(socialType)
                    .build();
            SocialResponse socialResponseDto = new SocialResponse("SIGNUP",account);
            return SocialResponseMessage.builder().message(
                    Message.builder()
                            .status(StatusEnum.OK)
                            .msg("소셜 회원가입 진행중")
                            .build()
            ).data(socialResponseDto).build();
        }
    }

    @Transactional
    public TokenMessage socialSignUp(SocialSignUpRequest requestDto){
        if (accountRepository.existsByEmail(requestDto.getEmail())){
            throw new DuplicateException(Content.EMAIL_DUPLICATE, StatusEnum.BAD_REQUEST);
        }
        if (accountRepository.existsByNickname(requestDto.getNickname())){
            throw new DuplicateException(Content.NICKNAME_DUPLICATE,StatusEnum.BAD_REQUEST);
        }
        Account account = requestDto.toAccount(passwordEncoder,suffix);
        accountRepository.save(account);
        TokenDto tokenDto = login(account.toAccountRequestDto(suffix));
        return TokenMessage.builder()
                .message(
                        Message.builder().
                                msg("소셜 회원가입")
                                .status(StatusEnum.OK)
                                .build()
                )
                .data(tokenDto).build();
    }

    @Transactional
    public TokenMessage normalLogin(UserRequest accountRequestDto){
        return TokenMessage.builder()
                .message(
                        Message.builder().
                                msg("기본 로그인")
                                .status(StatusEnum.OK)
                                .build()
                )
                .data(login(accountRequestDto)).build();
    }

    @Transactional
    public UserResponseMessage normalSignup(UserRequest accountRequestDto){
        accountRequestDto.updateSocialType(SocialType.NORMAL);
        return UserResponseMessage.builder()
                .message(
                        Message.builder().
                                msg("회원가입 축하드립니다")
                                .status(StatusEnum.OK)
                                .build()
                )
                .data(signup(accountRequestDto)).build();
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
        return Message.of(StatusEnum.OK,"로그아웃 되었습니다.");
    }

    public UserResponse signup(UserRequest accountRequestDto){
        if (accountRepository.existsByEmail(accountRequestDto.getEmail())){
            throw new DuplicateException(Content.EMAIL_DUPLICATE, StatusEnum.BAD_REQUEST);
        }
        if (accountRepository.existsByNickname(accountRequestDto.getNickname())){
            throw new DuplicateException(Content.NICKNAME_DUPLICATE,StatusEnum.BAD_REQUEST);
        }
        if (accountRequestDto.getSocialType().equals(SocialType.NORMAL) && !PasswordUtil.validPassword(accountRequestDto.getPassword())){
                throw new NotValidateException(Content.NOT_VAILDATION_PASSWORD, StatusEnum.BAD_REQUEST);
        }
        Account account = accountRequestDto.toAccount(passwordEncoder);
        return UserResponse.of(accountRepository.save(account));
    }

    public TokenDto login(UserRequest accountRequestDto){
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
    public TokenMessage reissue(TokenRequestDto tokenRequestDto){
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
        return TokenMessage.builder()
                .message(
                        Message.builder().
                                msg("토큰 재발급")
                                .status(StatusEnum.OK)
                                .build()
                )
                .data(tokenDto).build();
    }

    @Transactional(readOnly = true)
    public Message existByNickname(String nickname){
        if (accountRepository.existsByNickname(nickname))
            throw new DuplicateException(Content.NICKNAME_DUPLICATE, StatusEnum.BAD_REQUEST);
        return Message.of("중복되는 닉네임이 없습니다.");
    }

}