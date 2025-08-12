package com.example.ex3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Ex3Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex3Application.class, args);
		System.out.println("http://localhost:8080/ex3");
	}

}