package com.yapp.project.account.service;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.dto.*;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.PrefixType;
import com.yapp.project.aux.StatusEnum;
import com.yapp.project.aux.content.AccountContent;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.config.exception.account.*;
import com.yapp.project.config.jwt.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@WithMockUser(value = AccountTemplate.EMAIL,password = AccountTemplate.PASSWORD)
class AuthServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Test
    @Transactional
    void test_일반_회원가입_성공했을_때(){
        //given
        AccountDto.UserRequest request = AccountTemplate.makeAccountRequestDto();
        //when
        SocialDto.TokenMessage message = authService.normalSignUp(request);
        //then
        assertThat(message.getData().getAccessToken()).isNotNull();
    }


    @Test
    @Transactional
    void test_일반_회원가입_실패했을_때(){
        //given
        AccountDto.UserRequest request = AccountTemplate.makeAccountRequestDto(AccountTemplate.EMAIL,
                AccountTemplate.USERNAME,"1234");
        //when -> then
        assertThatThrownBy(() ->authService.normalSignUp(request)).isInstanceOf(PasswordInvalidException.class)
                .hasMessage(AccountContent.NOT_VAILDATION_PASSWORD);
    }


    @Test
    @Transactional
    void test_소셜_회원가입_성공했을_때(){
        //given
        SocialDto.SocialSignUpRequest request = AccountTemplate.makeSocialSignUpRequest();
        //when
        SocialDto.TokenMessage message = authService.socialSignUp(request);
        //then
        assertThat(tokenProvider.validateToken(message.getData().getAccessToken())).isTrue();
    }


    @Test
    @Transactional
    void signupSuccess() {
        //given
        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto("hello@example.com");
        //when
        SocialDto.TokenMessage tokenMessage = authService.normalSignUp(accountRequestDto);
        //then
        String token = tokenMessage.getData().getAccessToken();
        assertThat(tokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @Transactional
    void signupFail() {
        //given
        Account account = AccountTemplate.makeTestAccount();
        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto();
        //when
        accountRepository.save(account);
        //then
        assertThatThrownBy(() -> authService.normalSignUp(accountRequestDto)).isInstanceOf(EmailDuplicateException.class)
                .hasMessage(AccountContent.EMAIL_DUPLICATE);
    }

    @Test
    @Transactional
    void loginSuccess() {
        //given
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);
        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto();
        //when
        TokenDto tokenDto = authService.login(accountRequestDto);
        //then
        assertThat(tokenDto.getAccessToken()).isNotNull();
        assertThat(tokenDto.getRefreshToken()).isNotNull();
    }

    @Test
    @Transactional
    void loginFail(){
        //given
        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto("fail@example.com"
                ,"fail");
        //when -> then
        assertThatThrownBy(() ->authService.login(accountRequestDto)).isInstanceOf(NotFoundUserInformationException.class)
                .hasMessage(AccountContent.NOT_FOUND_USER_INFORMATION);
    }

    @Test
    @Transactional
    void reissue() throws InterruptedException {
        //given
        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto();
        accountRepository.save(AccountTemplate.makeTestAccount());
        TokenDto tokenDto = authService.login(accountRequestDto);
        TokenRequestDto tokenRequestDto = new TokenRequestDto(tokenDto.getRefreshToken());
        TimeUnit.MICROSECONDS.sleep(1);
        //when
        SocialDto.TokenMessage reissueToken = authService.reissue(tokenRequestDto);
        //then
        assertThat(tokenDto.getAccessTokenExpiresIn()-reissueToken.getData().getAccessTokenExpiresIn()).isNegative();
    }

    @Test
    @Transactional
    void logout(){
        //given
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);
        //when
        Message message = authService.logout();
        //then
        assertThat(message.getStatus()).isEqualTo(StatusEnum.ACCOUNT_OK);
    }

    @Test
    @Transactional
    void test_인증번호_서버에서_보냈을_때(){
        //given
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);
        String email = account.getEmail();
        AccountDto.EmailRequest request = new AccountDto.EmailRequest(email);
        //when
        Message message = authService.sendAuthenticationNumber(request);
        //then
        assertThat(message.getMsg()).isEqualTo(AccountContent.ACCOUNT_OK_MSG);
        assertThat(message.getStatus()).isEqualTo(StatusEnum.ACCOUNT_OK);
    }

    @Test
    @Transactional
    void test_인증번호_인증_성공했을_때(){
        //given
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);
        String email = account.getEmail();
        AccountDto.EmailRequest emailRequest = new AccountDto.EmailRequest(email);
        authService.sendAuthenticationNumber(emailRequest);

        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String number = valueOperations.get(PrefixType.PREFIX_TEMP_NUMBER+email);
        AccountDto.AuthenticationNumberRequest request = new AccountDto.AuthenticationNumberRequest(email, number);
        //when
        Message message = authService.checkAuthenticationNumber(request);
        //then
        assertThat(message.getMsg()).isEqualTo(AccountContent.ACCOUNT_OK_MSG);
        assertThat(message.getStatus()).isEqualTo(StatusEnum.ACCOUNT_OK);
    }

    @Test
    @Transactional
    void test_비밀번호_재설정_성공했을_때(){
        //given
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);
        String email = account.getEmail();
        AccountDto.ResetPasswordRequest request = new AccountDto.ResetPasswordRequest(email, "NewPwd12$@#");
        //when
        Message message = authService.resetPassword(request);
        //then
        assertThat(message.getMsg()).isEqualTo(AccountContent.ACCOUNT_OK_MSG);
        assertThat(message.getStatus()).isEqualTo(StatusEnum.ACCOUNT_OK);

    }
}