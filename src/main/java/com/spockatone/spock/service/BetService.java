package com.spockatone.spock.service;

import com.spockatone.spock.dao.BetDao;
import com.spockatone.spock.entity.Lot;

import java.time.LocalDateTime;

public class BetService {
    private static int step = 5;//TODO get from properties
    private BetDao betDao;

    public BetService(BetDao betDao) {
        this.betDao = betDao;
    }

    public int getStep() {
        return step;
    }

    public void makeBet(int userId, int lotId, double price){
        LocalDateTime time = LocalDateTime.now();
        betDao.makeBet(userId, lotId, price, time);
    }
    public String getWinnerName(int lotId){
        String winner = betDao.getWinnerName(lotId);
        if (winner != null){
            return winner;
        }
        return "None";
    }

    public double getCurrentPrice(Lot lot) {
        double currentPrice = lot.getCurrentPrice();
        if (currentPrice != 0){
            return currentPrice * (1 + step/100);
        }
        return lot.getStartPrice()  * (1 + step/100);
    }
}