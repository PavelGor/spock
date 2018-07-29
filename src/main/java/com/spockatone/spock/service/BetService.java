package com.spockatone.spock.service;

import com.spockatone.spock.dao.BetDao;
import com.spockatone.spock.entity.Bet;
import com.spockatone.spock.dao.MessageDao;
import com.spockatone.spock.entity.Lot;

import java.time.LocalDateTime;

public class BetService {
    private int step;
    private BetDao betDao;
//    private MessageService messageService;

    public BetService(BetDao betDao) {
        this.betDao = betDao;
    }

    public int getStep() {
        return step;
    }

    public void makeBet(int userId, int lotId, double price) {
        LocalDateTime time = LocalDateTime.now();
        int betId = betDao.makeBet(userId, lotId, price, time);

//        messageService.sendMessages(userId, lotId, price);
        //TODO add messages to all
    }

    public String getWinnerName(int lotId) {
        String winner = betDao.getWinnerName(lotId);
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
}