package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
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
import javax.xml.crypto.Data;
import java.util.List;

@Repository
public class UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations insert;

    public UserDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        insert = new SimpleJdbcInsert(dataSource)
                .withTableName("user");
    }

    public boolean insertUser(User user) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(user);
        int result = insert.execute(params);
        return result == 1;
    }

    public boolean deleteUser(int userId){
        String sql = "delete from user where user_id = :userId";
        SqlParameterSource params = new MapSqlParameterSource("user_id", userId);
        int result = jdbcTemplate.update(sql, params);
        return  result == 1;
    }

    public boolean updateUser(String email, String name, String password, int userId){
        String sql = "update user set email = :email, name = :name, password = :password where user_id= :userId";
//        SqlParameterSource params = new MapSqlParameterSource().addValue("email", email).addValue("name", name).addValue("password", password);
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setUserId(userId);
        SqlParameterSource params = new BeanPropertySqlParameterSource(user);
        int result = jdbcTemplate.update(sql, params);
        return result == 1;
    }


}