package com.spockatone.spock.dao.jdbc;

import com.spockatone.spock.dao.BetDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class JdbcBetDao implements BetDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcBetDao.class);
    private static final String INSERT_BET_SQL = "INSERT INTO bets (user_id, lot_id, price, time) values (?, ?, ?, ?);";
    private DataSource dataSource;

    public JdbcBetDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void makeBet(int userId, int lotId, double price, LocalDateTime time) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BET_SQL)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, lotId);
            preparedStatement.setDouble(3, price);
            preparedStatement.setString(4, time.toString());
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOG.error("JdbcBetDao cannot make the bet", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getWinnerName(int lotId) { //TODO realise!
        return null;
    }
}
