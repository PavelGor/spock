package com.spockatone.spock.dao.jdbc;

import com.spockatone.spock.dao.UserDao;
import com.spockatone.spock.dao.jdbc.mapper.UserRawMapper;
import com.spockatone.spock.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserDao implements UserDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcUserDao.class);
    private static final String GET_USER_BY_LOGIN_SQL = "SELECT * from users where login = ?;";
    private UserRawMapper userRawMapper = new UserRawMapper();
    private DataSource dataSource;

    public JdbcUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User getByLogin(String login) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_LOGIN_SQL)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    LOG.error("there is no such user with login = " + login);
                    throw new RuntimeException("there is no such user with login" + login);
                }
                return userRawMapper.mapRaw(resultSet);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find user with login: " + login, e);
            throw new RuntimeException(e);
        }
    }
}
