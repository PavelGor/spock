package com.spockatone.spock.service;

import com.spockatone.spock.dao.BetDao;
import com.spockatone.spock.entity.Bet;
import com.spockatone.spock.entity.Lot;

import java.time.LocalDateTime;
import java.util.List;

public class BetService {
    private static final UserService USER_SERVICE = new UserService();
    private static final MessageService MESSAGE_SERVICE = new MessageService();
    private int step;

    private BetDao betDao;


    public int getStep() {
        return step;
    }

    public void makeBet(int userId, int lotId, double price) {
        LocalDateTime time = LocalDateTime.now();
        int betId = betDao.makeBet(userId, lotId, price, time);

        MESSAGE_SERVICE.sendMessagesAfterBet(betId);
    }

    public String getWinnerName(int lotId) {
        int winnerId = betDao.getWinnerUserId(lotId);
        String winner = USER_SERVICE.getUserNameById(winnerId);
        if (winner != null) {
            return winner;
        }
        return "None";
    }

    public double getPriceForBet(Lot lot) {
        double currentPrice = lot.getCurrentPrice();
        if (currentPrice != 0) {
            return currentPrice * (1 + step / 100);
        }
        return lot.getStartPrice() * (1 + step / 100);
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Bet getBetById(int id) {
        return betDao.getBetById(id);
    }

    public List<Integer> getFailedUsersByBetId(int lotId){
        return betDao.getFailedUsersByBetId(lotId);
    }

    public void setBetDao(BetDao betDao) {
        this.betDao = betDao;
    }
}