package com.spockatone.spock.dao;

import com.spockatone.spock.entity.Bet;

import java.time.LocalDateTime;
import java.util.List;

public interface BetDao {
    int makeBet(int userId, int lotId, double price, LocalDateTime time);
    int getWinnerUserId(int lotId);
    Bet getBetById(int id);
    List<Integer> getFailedUsersByBetId(int betId);
}