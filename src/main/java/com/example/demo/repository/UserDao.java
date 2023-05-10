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
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
@Repository
public class UserDao {


    private final NamedParameterJdbcTemplate jdbcTemplate;
    public UserDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    public User getUser(int userId) {
        String sql = "SELECT * FROM USER WHERE = :userId";
        SqlParameterSource params = new MapSqlParameterSource("userId", userId);
        RowMapper<User> userRowMapper = BeanPropertyRowMapper.newInstance(User.class);
        return  jdbcTemplate.queryForObject(sql, params, userRowMapper);
    }

    public List<User> getUsers() {
        String sql = "SELECT * FROM USER ORDER BY user_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    public boolean addUser(User user){
        String sql = "INSERT INTO USER VALUES(:userId, :email, :name, :password)";
        SqlParameterSource params = new MapSqlParameterSource("user",user);
        int result = jdbcTemplate.update(sql, params);
        return result == 1;
    }

    public boolean deleteUser(int userId){
        String sql = "DELETE * FROM USER WHERE = :userId";
        SqlParameterSource params = new MapSqlParameterSource("userId", userId);
        int result = jdbcTemplate.update(sql, params);
        return result  == 1;
    }
}
