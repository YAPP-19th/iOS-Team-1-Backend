package com.yapp.project;

import com.yapp.project.account.domain.repository.AccountRepository;
import com.yapp.project.aux.test.account.AccountTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(AccountRepository accountRepository){
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				accountRepository.save(AccountTemplate.makeTestAccount("first","first@example.com"));
			}
		};
	}

}
