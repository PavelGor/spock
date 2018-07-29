package com.spockatone.spock.dao.jdbc;

import com.spockatone.spock.dao.MessageDao;
import com.spockatone.spock.dao.jdbc.mapper.MessageRawMapper;
import com.spockatone.spock.entity.Message;
import com.spockatone.spock.entity.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcMessageDao implements MessageDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcMessageDao.class);
    private static final MessageRawMapper MESSAGE_RAW_MAPPER = new MessageRawMapper();


    private static final String GET_BY_USER_ID_SQL = "SELECT * FROM messages WHERE user_id = ?;";
    private static final String INSERT_MESSAGE_SQL = "INSERT INTO messages (user_id, type, message_text) values (?, ?, ?);";
    private static final String UPDATE_VIEWED_SQL = "UPDATE messages SET viewed = TRUE where id = ?";

    private DataSource dataSource;

    public JdbcMessageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Message> getMessagesByUserId(int userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_USER_ID_SQL)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Message> messages = new ArrayList<>();
                while (resultSet.next()) {
                    messages.add(MESSAGE_RAW_MAPPER.mapRaw(resultSet));
                }
                return messages;
            }
        } catch (SQLException e) {
            LOG.error("DB error during obtaining messages by id = {}", userId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertMessage(int userId, Type type, String messageText) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MESSAGE_SQL)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, type.toString());
            preparedStatement.setString(3, messageText);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOG.error("DB error during inserting message, with next text: {}", messageText, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void messageViewed(int messageId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_VIEWED_SQL)) {
            preparedStatement.setInt(1, messageId);
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            LOG.error("DB error during marking message as viewed, messageId = {}", messageId, e);
            throw new RuntimeException(e);
        }
    }
}
