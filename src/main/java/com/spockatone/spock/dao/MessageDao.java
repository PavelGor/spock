package com.spockatone.spock.dao;

import com.spockatone.spock.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageDao {
    List<Message> getMessagesByUserId(int userId);

    void insertMessage(int userId, String type, String messageText, LocalDateTime betTime);

    void messageViewed(int messageId);
}
