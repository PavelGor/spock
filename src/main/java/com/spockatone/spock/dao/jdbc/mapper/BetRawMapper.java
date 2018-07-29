package com.spockatone.spock.dao.jdbc.mapper;

import com.spockatone.spock.entity.Bet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BetRawMapper {
    public Bet mapRaw(ResultSet resultSet) throws SQLException {
        Bet bet = new Bet();

        bet.setId(resultSet.getInt("id"));
        bet.setLotId(resultSet.getInt("lot_id"));
        bet.setUserId(resultSet.getInt("user_id"));
        bet.setPrice(resultSet.getDouble("price"));

        Timestamp betTime = resultSet.getTimestamp("time");
        bet.setTime(betTime.toLocalDateTime());

        return bet;
    }
}
