package com.spockatone.spock.dao;

import com.spockatone.spock.entity.Message;

import java.util.List;

public interface MessageDao {
    List<Message> getMessagesByUserId(int userId);

    void insertMessage(int userId, String type, String messageText);

    void messageViewed(int messageId);
}
