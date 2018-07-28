package com.spockatone.spock.dao.jdbc.mapper;

import com.spockatone.spock.entity.Lot;
import com.spockatone.spock.entity.Status;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class LotRawMapper {
    public Lot mapRaw(ResultSet resultSet) throws SQLException {
        Lot lot = new Lot();

        lot.setId(resultSet.getInt("id"));
        lot.setName(resultSet.getString("name"));
        lot.setDescription(resultSet.getString("description"));
        lot.setStartPrice(resultSet.getDouble("start_price"));
        lot.setCurrentPrice(resultSet.getDouble("current_price"));

        Timestamp startTime = resultSet.getTimestamp("start_time");
        lot.setStartTime(startTime.toLocalDateTime());
        Timestamp  endTime = resultSet.getTimestamp("end_time");
        lot.setEndTime(endTime.toLocalDateTime());

        lot.setPictureLink(resultSet.getString("picture_link"));
        lot.setStatus(Status.getById(resultSet.getString("status")));

        return lot;
    }
}
