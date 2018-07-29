package com.spockatone.spock.dao;

import com.spockatone.spock.entity.Message;
import com.spockatone.spock.entity.Type;

import java.util.List;

public interface MessageDao {
    List<Message> getMessagesByUserId(int userId);

    void insertMessage(int userId, Type type, String messageText);

    void messageViewed(int messageId);
}
