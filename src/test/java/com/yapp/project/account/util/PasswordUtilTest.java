package com.yapp.project.account.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
class PasswordUtilTest {

    @Test
    void 정규식에_맞게_비밀번호를_작성했을_때(){
        String password = "Test12#$%";
        assertThat(PasswordUtil.validPassword(password)).isTrue();
    }

    @Test
    void 글자수가_8글자_미만일_때(){
        String password = "Test12";
        assertThat(PasswordUtil.validPassword(password)).isFalse();
    }

    @Test
    void 숫자와_특수문자가_없을때(){
        String password = "testtest";
        assertThat(PasswordUtil.validPassword(password)).isFalse();
    }

    @Test
    void 숫자가_없을때(){
        String password = "test!!@@";
        assertThat(PasswordUtil.validPassword(password)).isFalse();
    }

    @Test
    void 특수문자가_없을때(){
        String password = "test1234";
        assertThat(PasswordUtil.validPassword(password)).isFalse();
    }

}