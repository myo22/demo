package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// @Componenent 어노테이션이 붙어있는 객체는 스프링 컨테이너가 관리하는 객체가 된다. -> Bean
@SpringBootApplication // 설정파일이면서도 컨퍼넌트이기도 하다.
public class DemoApplication implements CommandLineRunner { // 고로 DemoApplication 클래스 자체가 설정파일이다.

	// main메소드는 Spring이 관리 안한다. SpringApplication.run 이후가 Spring이 관리하므로
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// DataSource Bean(Spring이 관리하는 객체) -> 이것을 이용해야지 데이터베이스 커넥션을 얻어와서 프로그래밍을 할 수 있다.
	@Autowired //DataSource Bean을 주입한다.
	DataSource dataSource;

	// Spring이 Object로 참조할 수 있는 모든 Bean을 주입한다.
	@Autowired
	List<Object> beans;

	// 스프링부트에서는 CommandLineRunner을 구현하고 있는 run이 프로그램 시작점이다.
	@Override
	public void run(String... args) throws Exception {
		System.out.println("스프링 부트가 관리하는 빈을 사용할 수 있다.");
//		for(Object obj: beans){
//			System.out.println(obj.getClass().getName());
//		}
//		System.out.println(dataSource.getClass().getName());
//		System.out.println("--------------");
//		Connection connection = dataSource.getConnection(); // db연결을 달라고 할 수 있다. dataSource를 통해 커넥션을 얻어온것.

		List<Connection> list = new ArrayList<>();

		int i = 0;
		while (true){
			Connection conn = dataSource.getConnection();
			list.add(conn);
			System.out.println("Connection " + i + " : " + conn);
			i++;
			// conn을 이용하여 SQL을 실행. slow sql을 실행하게 되면... close가 느리게 된다.
			conn.close(); // 이렇게 커넥션을 되돌려줘야 프로그램이 안멈춘다.
			Thread.sleep(100); // 0.1초 쉰다.
		}

//		// JDBC프로그래밍과 관련된 부분
//		PreparedStatement ps = connection.prepareStatement("select role_id, name from role"); // select 문장을 실행할 준비를 한후 PreparedStatement를 받고
//		ResultSet rs = ps.executeQuery(); // PreparedStatement 실행을 하고 그건 dbms 안에 있으니 그 결과를 한건씩 읽어와서
//		while(rs.next()){
//			int roleId = rs.getInt("role_id"); // 그걸 칼럼별로 쪼개서
//			String name = rs.getString("name"); // 읽어서
//			System.out.println(roleId + ", " + name); // 출력
//		}
//		rs.close();
//		ps.close();
//		connection.close(); // 커넥션 풀에 되돌려주도록 구현해논것이다. 끊어지는것 X
	}
}
