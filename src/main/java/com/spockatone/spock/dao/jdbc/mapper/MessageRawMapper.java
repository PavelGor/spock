package com.spockatone.spock.dao.jdbc.mapper;

import com.spockatone.spock.entity.Message;
import com.spockatone.spock.entity.Type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageRawMapper {
    public Message mapRaw(ResultSet resultSet) throws SQLException {
        Message message = new Message();

        message.setId(resultSet.getInt("id"));
        message.setType(Type.getById(resultSet.getString("type")));
        message.setMessageText(resultSet.getString("message_text"));
        message.setUserId(resultSet.getInt("user_id"));
        message.setViewed(resultSet.getBoolean("viewed"));

        return message;
    }
}
