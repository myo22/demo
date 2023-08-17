package com.example.demo.repository;

import com.example.demo.domain.Role;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

//  Spring JDBC를 이용해서 Database프로그래밍
// @Repository는 @Component이다. -> 컨테이너가 관리해주는 Bean이다.
@Repository
public class RoleDao {
//    private final JdbcTemplate jdbcTemplate; // 필드를 final로 선언하면 반드시 생성자에서 초기화한다.
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private SimpleJdbcInsertOperations insertAction; // insert를 쉽게 하도록 도와주는 인터페이스

    // 생성자에 파라미터를 넣어주면 스프링 부트가 자동으로 주입한다. -> 생성자 주입
    public RoleDao(DataSource dataSource){
        System.out.println("RoleDao 생성자 호출");
        System.out.println(dataSource.getClass().getName());
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource); // DataSource를 넣어줘야 사용이 가능하다.
        insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("role"); // 자동으로 생성된 insert문장 테이블에 Role 테이블 지정. -> 즉 insert할 테이블 이름을 넣어준다.
    }

    // Role 테이블에 한건 저장. 저장을 성공하면 true, 실패하면 false를 반환한다.
//    public boolean addRole(Role role){
//        String sql = "INSERT INTO role(role_id, name) VALUES(?, ?)";
//        int result = jdbcTemplate.update(sql, role.getRoleId(), role.getName()); // update메소드는 insert, update, delete SQL문을 실행할 때 사용한다.
//        return result == 1; // 1과 같으면 저장이 된거다.
//    }

    public boolean addRole(Role role){
        // role은 프로퍼티 roleId, name을 가지고 있다.
        // INSERT INTO role(role_id, name) VALUES(:roleID, :name)
        // 위와 같은 SQL을 SimpleJdbcInsert가 내부적으로 만든다.
        // Role클래스의 프로퍼티 이름과 컬럼명이 규칙이 맞아야 한다. 예) role_id 컬럼명 = roleId 프로퍼티
        // BeanPropertySqlParameterSource에 role의 값을 담아주게 되면 사용할수 있는 객체인 params를 이용가능
        SqlParameterSource params = new BeanPropertySqlParameterSource(role);
        int result = insertAction.execute(params); // 넣어주기만 하면 insert 마침.
        return result == 1; // 성공하면 1반환하니까 true
    }

    public boolean deleteRole(int roleId){
        String sql = "DELETE FROM role WHERE role_id = :roleId"; // ?은 물음표에 값을 채워주는 것인데 -> 대신 :roleId은 프로퍼티값을 채워주도록 해라
        SqlParameterSource params = new MapSqlParameterSource("roleId", roleId);
        int result = jdbcTemplate.update(sql, params);
        return result == 1;
    }

    public Role getRole(int roleId){
        String sql = "SELECT role_id, name FROM role WHERE role_id = :roleId"; // role_id는 PK이기 때문에 1건 or 0건의 데이터가 조회된다.(유일성)
        // queryForObject는 1건 또는 0건을 읽어오는 메소드.(위에 셀렉트문처럼 결과가 있거나 없거나하는 셀렉트문을 실행하는것에 사용)
        // queryForObject(String sql, RowMapper<T> rowMapper, @Nullable Object... args) @Nullable Object... args는 여러개의 원소가 세번째 인자로 올 수 있다.
        try {
            SqlParameterSource params = new MapSqlParameterSource("roleId", roleId);
            // 람다 표현식을 이 한줄로 대체한다.
            RowMapper<Role> roleRowMapper = BeanPropertyRowMapper.newInstance(Role.class);
            return jdbcTemplate.queryForObject(sql, params, roleRowMapper);
        }catch (Exception e){
            return null;
        }
    }

//    public Role getRole(int roleId){
//        String sql = "SELECT role_id, name FROM role WHERE role_id = ?"; // role_id는 PK이기 때문에 1건 or 0건의 데이터가 조회된다.(유일성)
//        // queryForObject는 1건 또는 0건을 읽어오는 메소드.(위에 셀렉트문처럼 결과가 있거나 없거나하는 셀렉트문을 실행하는것에 사용)
//        // queryForObject(String sql, RowMapper<T> rowMapper, @Nullable Object... args) @Nullable Object... args는 여러개의 원소가 세번째 인자로 올 수 있다.
//        try {
//            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
//                Role role = new Role();
//                role.setRoleId(rs.getInt("role_id"));
//                role.setName(rs.getString("name"));
//                return role;
//            }, roleId);
//        }catch (Exception e){
//            return null;
//        }
//    }

    public List<Role> getRoles(){
        String sql = "SELECT role_id, name FROM role ORDER BY role_id";
        // query메소드는 여러건의 결과를 구할때 사용하는 메소드 queryForObject는 한건만 리턴해주는데 query는 list에 담아서 여러개 리턴해준다.
//        return jdbcTemplate.query(sql, (rs, rowNum) -> {
//            Role role = new Role();
//            role.setRoleId(rs.getInt("role_id"));
//            role.setName(rs.getString("name"));
//            return role;
//        });
        // 이렇게 람다식을 대체 가능하다.
        RowMapper<Role> roleRowMapper = BeanPropertyRowMapper.newInstance(Role.class);
        return jdbcTemplate.query(sql, roleRowMapper);
    }

    // 아래 RoleRowMapper를 작성한후 이렇게 적용하면 위에 람다식이랑 똑같은 것이다.
    // return jdbcTemplate.queryForObject(sql, RoleRowMapper(),roleId);

    // 위에 RowMapper는 인터페이스 함수(FunctionalInterface)라서 람다식으로 표현가능한데 위에처럼 람다로 표현 안한다면 아래와 같다.
    // RowMapper는 메소드를 하나 가지고있다.
    // 데이터를 한건 읽어오는 것을 성공한 것을 가정하고. 한건의 데이터를 Role객체에 담아서 리턴하도록 코딩한것.
    // 이 클래스가 다른 클래스에서 전혀 재사용하는 일이 없다면, 클래스를 만들 필요까지 있을까?
    class RoleRowMapper implements RowMapper<Role> {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setRoleId(rs.getInt("role_id"));
            role.setName(rs.getString("name"));
            return role;
        }
    }

    /* 이 긴줄을 Spring JDBC를 이용하면 위 Select처럼 간단하게 가능
    List<Role> list = new ArrayList<Role>();
    아래는 sql을 준비 (바인딩)
    String sql = "SELECT role_id FROM role WHERE role_id = ?"; ?빼고 sql문 작성
    ps = conn.prepareStatement(sql); sql문 준비
    ps.setDouble(1, roleId); 물음표에 해당하는 값을 채우는것.
    sql을 실행
    int updateCount = ps.executeQuery();
    sql의 결과를 한건 읽어온다.
    while(rs.next()){ //role_id, name --> rs
        // 칼럼별로 쪼개서 DTO에 담는다.
        Role role = new Role();
        role.setRoleId(rs.getInt("role_id"));
        role.setName(rs.getString("name));
        list.add(role);
    }
    return list;
     */

}
