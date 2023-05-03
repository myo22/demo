package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@SpringBootApplication // 설정파일이면서도 컨퍼런트이기도 하다.
public class DemoApplication implements CommandLineRunner { // 고로 DemoApplication 클래스 자체가 설정파일이다.

	// main메소드는 Spring이 관리 안한다. SpringApplication.run 이후가 Spring이 관리하므로
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// DataSource Bean(Spring이 관리하는 객체) -> 이것을 이용해야지 데이터베이스 커넥션을 얻어와서 프로그래밍을 할 수 있다.
	@Autowired // 자동으로 주입
	DataSource dataSource;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("스프링 부트가 관리하는 빈을 사용할 수 있다.");

		Connection connection = dataSource.getConnection(); // db연결을 달라고 할 수 있다.

		// JDBC프로그래밍
		PreparedStatement ps = connection.prepareStatement("select role_id, name from role"); // select 문장을 실행할 준비를 한후 PreparedStatement를 받고
		ResultSet rs = ps.executeQuery(); // PreparedStatement 실행을 하고 그건 dbms 안에 있으니 그 결과를 한건씩 읽어와서
		while(rs.next()){
			int roleId = rs.getInt("role_id"); // 그걸 칼럼별로 쪼개서
			String name = rs.getString("name"); // 읽어서
			System.out.println(roleId + ", " + name); // 출력
		}
		rs.close();
		ps.close();
		connection.close();
	}
}
