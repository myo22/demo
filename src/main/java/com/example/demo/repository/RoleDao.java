package com.example.demo.repository;

import com.example.demo.domain.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

//  Spring JDBC를 이용해서 Database프로그래밍
// @Repository는 @Component이다. -> 컨테이너가 관리해주는 Bean이다.
@Repository
public class RoleDao {
    private final JdbcTemplate jdbcTemplate; // 필드를 final로 선언하면 반드시 생성자에서 초기화한다.

    // 생성자에 파라미터를 넣어주면 스프링 부트가 자동으로 주입한다. -> 생성자 주입
    public RoleDao(DataSource dataSource){
        System.out.println("RoleDao 생성자 호출");
        System.out.println(dataSource.getClass().getName());
        jdbcTemplate = new JdbcTemplate(dataSource); // DataSource를 넣어줘야 사용이 가능하다.
    }

    // Role 테이블에 한건 저장. 저장을 성공하면 true, 실패하면 false를 반환한다.
    public boolean addRole(Role role){
        String sql = "INSERT INTO role(role_id, name) VALUES(?, ?)";
        int result = jdbcTemplate.update(sql, role.getRoleId(), role.getName()); // update메소드는 insert, update, delete SQL문을 실행할 때 사용한다.
        return result == 1; // 1과 같으면 저장이 된거다.
    }

    public boolean deleteRole(int roleId){
        String sql = "DELETE FROM role WHERE role_id = ?";
        int result = jdbcTemplate.update(sql, roleId);
        return result == 1;
    }

    public Role getRole(int roleId){
        String sql = "SELECT role_id, name FROM role WHERE role_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Role role = new Role();
            role.setRoleId(rs.getInt("role_id"));
            role.setName(rs.getString("name"));
            return role;
        }, roleId);
    }

    /* 이 긴줄을 Spring JDBC를 이용하면 위처럼 간단하게 가능
    String sql = "INSERT INTO role(role_id, name) VALUES(?, ?, ?)";
    ps = conn.prepareStatement(sql);
    ps.setInt(1, emp.getID());
    ps.setString(2, emp.getName());
    ps.setDouble(3, emp.getSal());
    int updateCount = ps.executeUpdate();
     */
}
