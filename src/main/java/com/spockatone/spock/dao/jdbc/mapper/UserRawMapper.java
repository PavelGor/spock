package com.spockatone.spock.dao.jdbc.mapper;

import com.spockatone.spock.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRawMapper {
    public User mapRaw(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUserName(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setSole(resultSet.getString("salt"));
        return user;
    }
}
