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
import com.yapp.project.aux.common.DateUtil;
import com.yapp.project.aux.common.Utils;
import com.yapp.project.aux.content.AccountContent;
import com.yapp.project.config.exception.account.*;
import com.yapp.project.config.jwt.TokenInfo;
import com.yapp.project.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.yapp.project.aux.content.AccountContent.ACCOUNT_OK_MSG;
import static com.yapp.project.aux.content.AccountContent.EMAIL_SUBJECT;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String,String> redisTemplate;
    private final JavaMailSender javaMailSender;

    @Value("${social.value}")
    private String suffix;

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Value("${property.status}")
    private String profile;

    @Transactional(readOnly = true)
    public SocialResponseMessage socialAccess(SocialRequest socialRequestDto){
        SocialType socialType = socialRequestDto.getSocial();
        String email = socialRequestDto.getEmail();
        Account account = accountRepository.findByEmail(email).orElse(null);
        if (account != null){
            TokenDto tokenDto = login(account.toAccountRequestDto(suffix));
            SocialResponse socialResponseDto = new SocialResponse("LOGIN",tokenDto);
            return socialResponseDto.toSocialResponseMessage(AccountContent.SOCIAL_LOGIN_SUCCESS);
        }else {
            MiddleAccount middleAccount = MiddleAccount.builder().email(email).socialType(socialType)
                    .build();
            SocialResponse socialResponseDto = new SocialResponse("SIGNUP", middleAccount);
            return socialResponseDto.toSocialResponseMessage(AccountContent.SOCIAL_SIGNUP_ING);
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
        return tokenDto.toTokenMessage(AccountContent.SOCIAL_SIGNUP_FINISH, StatusEnum.SOCIAL_OK);

    }


    @Transactional(readOnly = true)
    public TokenMessage normalLogin(UserRequest accountRequestDto){
        return login(accountRequestDto).toTokenMessage(AccountContent.NORMAL_LOGIN_SUCCESS, StatusEnum.ACCOUNT_OK);
    }


    @Transactional
    public TokenMessage normalSignUp(UserRequest accountRequestDto){
        accountRequestDto.updateSocialType(SocialType.NORMAL);
        signUp(accountRequestDto);
        return login(accountRequestDto).toTokenMessage(AccountContent.NORMAL_SIGNUP_SUCCESS, StatusEnum.ACCOUNT_OK);
    }


    @Transactional
    public Message logout(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountEmail = authentication.getName();
        if (accountRepository.findByEmail(accountEmail).isPresent()){
            redisTemplate.delete(PrefixType.PREFIX_REFRESH_TOKEN+authentication.getName());
        }else{
            throw new NotFoundUserInformationException();
        }
        return Message.of(StatusEnum.ACCOUNT_OK,AccountContent.LOGOUT_SUCCESS);
    }


    @Transactional(readOnly = true)
    public Message existByNickname(String nickname){
        if (accountRepository.existsByNickname(nickname))
            throw new NicknameDuplicateException();
        return Message.of(AccountContent.DOES_NOT_EXISTS_DUPLICATE_NICKNAME);
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
        redisTemplate.expire(key, Duration.ofSeconds(TokenInfo.REFRESH_TOKEN_EXPIRE_TIME));
        return tokenDto.toTokenMessage(AccountContent.TOKEN_REISSUE_SUCCESS, StatusEnum.TOKEN_OK);
    }


    @Transactional
    public Message sendAuthenticationNumber(EmailRequest request){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String email = request.getEmail();
        Account account = accountRepository.findByEmail(email).orElseThrow(NotFoundUserInformationException::new);
        String key = PrefixType.PREFIX_TEMP_NUMBER + account.getEmail();
        String number = Utils.authenticationNumber();
        valueOperations.set(key, number);
        redisTemplate.expire(key,Duration.ofSeconds(DateUtil.TEMP_NUMBER_SECONDS));
        sendEmail(email, number);
        return Message.of(StatusEnum.ACCOUNT_OK, ACCOUNT_OK_MSG);
    }


    @Transactional(readOnly = true)
    public Message checkAuthenticationNumber(AuthenticationNumberRequest request){
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String email = request.getEmail();
        String value = valueOperations.get(PrefixType.PREFIX_TEMP_NUMBER+email);
        assert value!=null;
        if (value.equals(request.getNumber())){
            return Message.of(StatusEnum.ACCOUNT_OK, ACCOUNT_OK_MSG);
        }else{
            throw new NotEqualAuthenticationNumberException();
        }
    }

    @Transactional
    public Message resetPassword(ResetPasswordRequest request){
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(NotFoundUserInformationException::new);
        SocialType socialType = account.getSocialType();
        String password = request.getPassword();
        checkPasswordValidation(socialType, password);
        account.resetPassword(passwordEncoder, request.getPassword());
        return Message.of(StatusEnum.ACCOUNT_OK, ACCOUNT_OK_MSG);
    }


    public void signUp(UserRequest accountRequestDto){
        String email = accountRequestDto.getEmail();
        String nickname = accountRequestDto.getNickname();
        checkDuplicateExceptionFields(email, nickname);
        SocialType socialType = accountRequestDto.getSocialType();
        String password = accountRequestDto.getPassword();
        checkPasswordValidation(socialType, password);
        Account account = accountRequestDto.toAccount(passwordEncoder);
        accountRepository.save(account);
    }


    private void sendEmail(String email, String number){
        if (profile.equals("test")){
            return;
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sendFrom);
        simpleMailMessage.setSubject(EMAIL_SUBJECT);
        simpleMailMessage.setText(number);
        javaMailSender.send(simpleMailMessage);
    }

    private void checkPasswordValidation(SocialType socialType, String password){
        if (socialType.equals(SocialType.NORMAL) && !PasswordUtil.validPassword(password)){
            throw new PasswordInvalidException();
        }
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
        redisTemplate.expire(key, Duration.ofSeconds(TokenInfo.REFRESH_TOKEN_EXPIRE_TIME));
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