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

import javax.persistence.EntityManager;
import java.util.concurrent.TimeUnit;

import static com.yapp.project.account.domain.dto.AccountDto.*;
import static com.yapp.project.account.domain.dto.SocialDto.*;
import static com.yapp.project.aux.test.account.AccountTemplate.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@WithMockUser(value = EMAIL,password = PASSWORD)
class AuthServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void test_이메일_인증_확인() {
        //given
        String email = EMAIL;
        //when
        EmailValidationResponseMessage message = authService.isAlreadyExistEmail(email);
        //then
        assertThat(message).isNotNull();
        assertThat(message.getData().isExist()).isFalse();

        //given
        UserRequest request = makeAccountRequestDto();
        authService.normalSignUp(request);
        //when
        EmailValidationResponseMessage message2 = authService.isAlreadyExistEmail(email);
        //then
        assertThat(message2).isNotNull();
        assertThat(message2.getData().isExist()).isTrue();


    }


    @Test
    @Transactional
    void test_일반_회원가입_성공했을_때(){
        //given
        UserRequest request = makeAccountRequestDto();
        //when
        TokenMessage message = authService.normalSignUp(request);
        //then
        assertThat(message.getData().getAccessToken()).isNotNull();

        Account account = accountRepository.findByEmail(EMAIL).orElse(null);
        assertThat(account).isNotNull();
        assertThat(account.getFcmToken()).isNotNull();
    }


    @Test
    @Transactional
    void test_일반_회원가입_실패했을_때(){
        //given
        UserRequest request = makeAccountRequestDto(EMAIL,
                USERNAME,"1234");
        //when -> then
        assertThatThrownBy(() ->authService.normalSignUp(request)).isInstanceOf(PasswordInvalidException.class)
                .hasMessage(AccountContent.NOT_VALIDATION_PASSWORD);
    }


    @Test
    @Transactional
    void test_소셜_회원가입_성공했을_때(){
        //given
        SocialSignUpRequest request = makeSocialSignUpRequest();
        //when
        TokenMessage message = authService.socialSignUp(request);
        //then
        assertThat(tokenProvider.validateToken(message.getData().getAccessToken())).isTrue();
    }


    @Test
    @Transactional
    void signupSuccess() {
        //given
        UserRequest accountRequestDto = makeAccountRequestDto("hello@example.com");
        //when
        TokenMessage tokenMessage = authService.normalSignUp(accountRequestDto);
        //then
        String token = tokenMessage.getData().getAccessToken();
        assertThat(tokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @Transactional
    void signupFail() {
        //given
        Account account = makeTestAccountForIntegration();
        UserRequest accountRequestDto = makeAccountRequestDto();
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
        Account account = makeTestAccountForIntegration();
        accountRepository.save(account);
        UserRequest accountRequestDto = makeAccountRequestDto();
        //when
        TokenDto tokenDto = authService.login(accountRequestDto.toLoginRequest());
        //then
        assertThat(tokenDto.getAccessToken()).isNotNull();
        assertThat(tokenDto.getRefreshToken()).isNotNull();
    }

    @Test
    @Transactional
    void loginFail(){
        //given
        UserRequest accountRequestDto = makeAccountRequestDto("fail@example.com"
                ,"fail");
        //when -> then
        assertThatThrownBy(() ->authService.login(accountRequestDto.toLoginRequest())).isInstanceOf(NotFoundUserInformationException.class)
                .hasMessage(AccountContent.NOT_FOUND_USER_INFORMATION);
    }

    @Test
    @Transactional
    void reissue() throws InterruptedException {
        //given
        UserRequest accountRequestDto = makeAccountRequestDto();
        accountRepository.save(makeTestAccount());
        TokenDto tokenDto = authService.login(accountRequestDto.toLoginRequest());
        TokenRequestDto tokenRequestDto = new TokenRequestDto(tokenDto.getRefreshToken());
        TimeUnit.MICROSECONDS.sleep(1);
        //when
        TokenMessage reissueToken = authService.reissue(tokenRequestDto);
        //then
        assertThat(tokenDto.getAccessTokenExpiresIn()-reissueToken.getData().getAccessTokenExpiresIn()).isNegative();
    }

    @Test
    @Transactional
    void logout(){
        //given
        Account account = makeTestAccount();
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
        Account account = makeTestAccountForIntegration();
        accountRepository.save(account);
        String email = account.getEmail();
        EmailRequest request = new EmailRequest(email);
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
        Account account = makeTestAccountForIntegration();
        accountRepository.save(account);
        String email = account.getEmail();
        EmailRequest emailRequest = new EmailRequest(email);
        authService.sendAuthenticationNumber(emailRequest);

        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String number = valueOperations.get(PrefixType.PREFIX_TEMP_NUMBER+email);
        AuthenticationNumberRequest request = new AuthenticationNumberRequest(email, number);
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
        Account account = makeTestAccountForIntegration();
        accountRepository.save(account);
        String email = account.getEmail();
        ResetPasswordRequest request = new ResetPasswordRequest(email, "NewPwd12$@#");
        //when
        Message message = authService.resetPassword(request);
        //then
        assertThat(message.getMsg()).isEqualTo(AccountContent.ACCOUNT_OK_MSG);
        assertThat(message.getStatus()).isEqualTo(StatusEnum.ACCOUNT_OK);

    }

    @Test
    @Transactional
    void test_평범한_로그인을_진행할_때() {
        //given
        Account account = makeTestAccountForIntegration();
        Account dbAccount = accountRepository.save(account);
        LoginRequest request = LoginRequest.builder().email(EMAIL).password(PASSWORD).fcmToken("변경된토큰").build();
        //when
        authService.normalLogin(request);
        entityManager.clear();
        Account afterAccount = accountRepository.findByEmail(dbAccount.getEmail()).orElse(null);
        //then
        assertThat(afterAccount).isNotNull();
        assertThat(afterAccount.getFcmToken()).isEqualTo("변경된토큰");
    }
}