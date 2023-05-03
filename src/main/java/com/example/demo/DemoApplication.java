package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 설정파일이면서도 컨퍼런스이다.
public class DemoApplication { // 고로 DemoApplication 클래스 자체가 설정파일이다.

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
