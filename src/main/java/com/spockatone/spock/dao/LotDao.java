package com.spockatone.spock.dao;

import com.spockatone.spock.entity.Lot;

import java.util.List;

public interface LotDao {
    List<Lot> getLotsByPage(int page, int itemsPerPage);

    int getLotsCount();

    Lot getLotById(int id);

}