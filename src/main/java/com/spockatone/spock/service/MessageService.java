package com.spockatone.spock.service;

import com.spockatone.spock.dao.MessageDao;
import com.spockatone.spock.entity.Message;
import com.spockatone.spock.entity.Type;

import java.util.List;

public class MessageService {
    private MessageDao messageDao;

    public MessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public List<Message> getMessagesByUserId(int userId) {
        List<Message> messages = messageDao.getMessagesByUserId(userId);
        for (Message message : messages) {
            messageViewed(message.getId());
        }
        return messages;
    }

    public void insertMessage(int userId, Type type, String messageText) {
        messageDao.insertMessage(userId, type.toString(), messageText);
    }

    public void messageViewed(int messageId) {
        messageDao.messageViewed(messageId);
    }

    public void sendMessagesAfterBet(int bet_id){

    }
}
