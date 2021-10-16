package com.yapp.project.saying.domain;

import com.yapp.project.saying.domain.repository.SayingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SayingTest {

    @Autowired
    private SayingRepository sayingRepository;

    @Test
    void test_content_없이_생성할때(){
        assertThatThrownBy(() -> Saying.builder().id(10000L).build()).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("내용은 반드시 있어야 합니다.");
    }

    @Test
    void test_author_생략했을_때(){
        Saying saying = Saying.builder().content("hello").build();
        Saying result = sayingRepository.save(saying);
        assertThat(result.getAuthor()).isEqualTo("작자미상");
    }

    @Test
    void test_author_넣었을_때(){
        Saying saying = Saying.builder().author("안녕").content("hello").build();
        Saying result = sayingRepository.save(saying);
        assertThat(result.getAuthor()).isEqualTo("안녕");
    }

}