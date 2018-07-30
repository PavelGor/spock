package com.spockatone.spock.service;

import com.spockatone.spock.dao.MessageDao;
import com.spockatone.spock.entity.Bet;
import com.spockatone.spock.entity.Message;
import com.spockatone.spock.entity.Type;

import java.time.LocalDateTime;
import java.util.List;

public class MessageService {
    private static final UserService USER_SERVICE = new UserService();

    private MessageDao messageDao;
    private BetService betService = new BetService();


    public List<Message> getMessagesByUserId(int userId) {
        List<Message> messages = messageDao.getMessagesByUserId(userId);
        for (Message message : messages) {
            messageViewed(message.getId());
        }
        return messages;
    }

    public void insertMessage(int userId, Type type, String messageText, LocalDateTime betTime) {
        messageDao.insertMessage(userId, type.toString(), messageText, betTime);
    }

    public void sendMessagesAfterBet(int bet_id) {
        Bet bet = betService.getBetById(bet_id);
        String leadingUser = USER_SERVICE.getUserNameById(bet.getUserId());
        LocalDateTime betTime = bet.getTime();
        String message = messageCreator(Type.SUCCESS, bet.getPrice(), leadingUser);
        insertMessage(bet.getUserId(), Type.SUCCESS, message, betTime);
        List<Integer> usersId = betService.getFailedUsersByBetId(bet.getLotId());
        for (Integer integer : usersId) {
            String failedMessageText = messageCreator(Type.FAILED, bet.getPrice(), leadingUser);
            insertMessage(integer, Type.FAILED, failedMessageText, betTime);
        }
    }

    public String messageCreator(Type type, double betPrice, String userName) {
        switch (type) {
            case SUCCESS:
                return "Ваша ставка " + betPrice + " UAH успешна.";
            case FAILED:
                return "Ваша ставка перебита пользователем " + userName + ". Новая цена " + betPrice + " UAH";
            case WIN:
                return "Вы выиграли данный лот ставкой " + betPrice + " UAH";
            case LOSE:
                return "Лот выиграл пользователь " + userName + "ставкой " + betPrice + " UAH";
            default:
                throw new IllegalArgumentException();
        }
    }

    public void messageViewed(int messageId) {
        messageDao.messageViewed(messageId);
    }

    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }
}
