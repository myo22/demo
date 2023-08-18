package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
    private final SimpleJdbcInsertOperations simpleJdbcInsert;


    public UserDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("User");
    }

    public boolean userInsert(User user){
//        String sql = "insert into user(email, name, password) values(:email, :name, :password)";
        SqlParameterSource parmas = new BeanPropertySqlParameterSource(user);
        int result =  simpleJdbcInsert.execute(parmas);
        return result == 1;
    }

    public boolean userUpdate(int user_id){
        String sql = "update user set email = :email, name = :name, password = :password where user_id=:userId";
        SqlParameterSource params = new MapSqlParameterSource("userId", user_id);
        int result = jdbcTemplate.update(sql, params);
        return result == 1;
    }

    public boolean userDelete(int user_id){
        String sql = "delete from user where user_id = :userId ";
        SqlParameterSource params = new MapSqlParameterSource("userId", user_id);
        int result =  jdbcTemplate.update(sql, params);
        return result == 1;
    }

    public List<User> userSelects(){
        String sql = "select * from user";
        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class);
        return jdbcTemplate.query(sql, rowMapper);
    }

    public User userSelect(int user_id){
        String sql = "select * from user where user_id = :userId ";
        SqlParameterSource params = new MapSqlParameterSource("userId", user_id);
        RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class);
        return jdbcTemplate.queryForObject(sql, params, rowMapper);
    }


}