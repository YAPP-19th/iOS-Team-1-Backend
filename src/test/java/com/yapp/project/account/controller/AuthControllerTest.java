package com.yapp.project.account.controller;

import com.yapp.project.account.domain.Account;
import com.yapp.project.account.domain.dto.AccountDto;
import com.yapp.project.account.domain.dto.SocialDto;
import com.yapp.project.account.domain.dto.TokenRequestDto;
import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.account.service.AuthService;
import com.yapp.project.aux.Message;
import com.yapp.project.aux.PrefixType;
import com.yapp.project.aux.content.AccountContent;
import com.yapp.project.aux.test.account.AccountTemplate;
import com.yapp.project.config.exception.account.*;
import com.yapp.project.config.jwt.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.yapp.project.aux.content.AccountContent.NORMAL_SIGNUP_SUCCESS;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@WithMockUser(value = AccountTemplate.EMAIL, password = AccountTemplate.PASSWORD)
class AuthControllerTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthController authController;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    @Transactional
    void test_회원가입_성공() {
        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto();
        ResponseEntity<SocialDto.TokenMessage> response = authController.signup(accountRequestDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage().getMsg()).isEqualTo(NORMAL_SIGNUP_SUCCESS);
    }

    @Test
    @Transactional
    void test_회원가입_실패(){
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);
        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto();
        assertThatThrownBy(() ->authController.signup(accountRequestDto)).isInstanceOf(EmailDuplicateException.class)
                .hasMessage(AccountContent.EMAIL_DUPLICATE);
    }

    @Test
    @Transactional
    void test_로그인_성공() {
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);
        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto();
        ResponseEntity<SocialDto.TokenMessage> response = authController.login(accountRequestDto);
        assertThat(response).isNotNull();
        assertThat(tokenProvider.validateToken(Objects.requireNonNull(response.getBody()).getData().getAccessToken())).isTrue();
    }

    @Test
    @Transactional
    void test_로그인_실페() {
        Account account = AccountTemplate.makeTestAccount("meaning","meaning@example.com");
        accountRepository.save(account);
        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto();
        assertThatThrownBy(() -> authController.login(accountRequestDto)).isInstanceOf(NotFoundUserInformationException.class)
                .hasMessage(AccountContent.NOT_FOUND_USER_INFORMATION);
    }

    @Test
    @Transactional
    void test_로그아웃_성공() {
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);
        ResponseEntity<Message> response = authController.logout();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Transactional
    void test_로그아웃_실패() {
        Account account = AccountTemplate.makeTestAccount("meaning","meaning@example.com");
        accountRepository.save(account);
        assertThatThrownBy(() -> authController.logout()).isInstanceOf(NotFoundUserInformationException.class)
                .hasMessage(AccountContent.NOT_FOUND_USER_INFORMATION);
    }

    @Test
    @Transactional
    void test_재발급_성공() {
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);

        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto();
        ResponseEntity<SocialDto.TokenMessage> response = authController.login(accountRequestDto);
        assertThat(response).isNotNull();
        assertThat(tokenProvider.validateToken(Objects.requireNonNull(response.getBody()).getData().getAccessToken())).isTrue();

        TokenRequestDto tokenRequestDto = new TokenRequestDto(response.getBody().getData().getRefreshToken());
        ResponseEntity<SocialDto.TokenMessage> reissueResponse = authController.reissue(tokenRequestDto);

        assertThat(reissueResponse).isNotNull();
        assertThat(reissueResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(tokenProvider.validateToken(
                Objects.requireNonNull(reissueResponse.getBody()).getData().getAccessToken())
        ).isTrue();
        assertThat(tokenProvider.validateToken(
                Objects.requireNonNull(reissueResponse.getBody()).getData().getRefreshToken())
        ).isTrue();
    }

    @Test
    @Transactional
    void test_재발급_토큰_정보_불일치로_인한_실패() {
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);

        TokenRequestDto tokenRequestDto = new TokenRequestDto("");
        assertThatThrownBy(() -> authController.reissue(tokenRequestDto)).isInstanceOf(RefreshTokenInvalidException.class)
                .hasMessage(AccountContent.REFRESH_TOKEN_INVALID);

    }

    @Test
    @Transactional
    void test_재발급_토큰_레디스에_토큰_저장이_안되어_있을_때() {
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);

        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto();
        ResponseEntity<SocialDto.TokenMessage> response = authController.login(accountRequestDto);
        assertThat(response).isNotNull();
        assertThat(tokenProvider.validateToken(Objects.requireNonNull(response.getBody()).getData().getAccessToken())).isTrue();

        redisTemplate.delete(PrefixType.PREFIX_REFRESH_TOKEN+AccountTemplate.EMAIL);

        TokenRequestDto tokenRequestDto = new TokenRequestDto(response.getBody().getData().getRefreshToken());
        assertThatThrownBy(() ->authController.reissue(tokenRequestDto)).isInstanceOf(AlreadyLogoutException.class)
                .hasMessage(AccountContent.LOGOUT_USER);
    }

    @Test
    @Transactional
    void test_재발급_요청된_RefreshToken과_Redis에_저장된_RefreshToken이_일치하지_않을_때() {
        Account account = AccountTemplate.makeTestAccount();
        accountRepository.save(account);

        AccountDto.UserRequest accountRequestDto = AccountTemplate.makeAccountRequestDto();
        ResponseEntity<SocialDto.TokenMessage> response = authController.login(accountRequestDto);
        assertThat(response).isNotNull();
        assertThat(tokenProvider.validateToken(Objects.requireNonNull(response.getBody()).getData().getAccessToken())).isTrue();

        // Redis에 저장된 리플레시 토큰 값을 빈 값으로 변경
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String key = PrefixType.PREFIX_REFRESH_TOKEN + AccountTemplate.EMAIL;
        valueOperations.set(key, "");

        TokenRequestDto tokenRequestDto = new TokenRequestDto(response.getBody().getData().getRefreshToken());
        assertThatThrownBy(() ->authController.reissue(tokenRequestDto)).isInstanceOf(TokenInvalidException.class)
                .hasMessage(AccountContent.TOKEN_NOT_EQUAL_USER_INFORMATION);

    }
}