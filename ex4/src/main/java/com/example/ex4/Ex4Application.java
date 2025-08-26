package com.example.ex4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
// AuditingEntityListener을 활성화 하기 위해 사용
@EnableJpaAuditing
public class Ex4Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex4Application.class, args);
		System.out.println("http://localhost:8080/ex4");

	}

}
