package com.spockatone.spock.dao;

import java.time.LocalDateTime;

public interface BetDao {
    void makeBet(int userId, int lotId, double price, LocalDateTime time);
    String getWinnerName(int lotId);
}