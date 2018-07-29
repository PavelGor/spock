package com.spockatone.spock.dao.jdbc;

import com.spockatone.spock.dao.BetDao;
import com.spockatone.spock.dao.jdbc.mapper.BetRawMapper;
import com.spockatone.spock.entity.Bet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDateTime;

public class JdbcBetDao implements BetDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcBetDao.class);
    private static final String INSERT_BET_SQL = "INSERT INTO bets (user_id, lot_id, price, time) values (?, ?, ?, ?);";
    private static final String GET_BET_BY_ID_SQL = "SELECT * FROM bets WHERE id = ?;";

    private static final BetRawMapper BET_RAW_MAPPER = new BetRawMapper();

    private DataSource dataSource;

    public JdbcBetDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int makeBet(int userId, int lotId, double price, LocalDateTime time) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BET_SQL)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, lotId);
            preparedStatement.setDouble(3, price);

            Timestamp sqlDateTime = Timestamp.valueOf(time);
            preparedStatement.setTimestamp(4,sqlDateTime);

            ResultSet resultSet = preparedStatement.executeQuery(); //TODO check
            return resultSet.getInt("id");
        } catch (SQLException e) {
            LOG.error("JdbcBetDao cannot make the bet", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bet getBetById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BET_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return BET_RAW_MAPPER.mapRaw(resultSet);
            }
        } catch (SQLException e) {
            LOG.error("DB error during obtaining bet by id , id = {}", id, e);
            throw new RuntimeException(e);
        }
    }


        @Override
        public String getWinnerName ( int lotId){ //TODO realise!
            return null;
        }
    }
