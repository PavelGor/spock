package com.spockatone.spock.dao;

import com.spockatone.spock.entity.Bet;

import java.time.LocalDateTime;

public interface BetDao {
    int makeBet(int userId, int lotId, double price, LocalDateTime time);
    String getWinnerName(int lotId);
    Bet getBetById(int id);
}