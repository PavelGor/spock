package com.spockatone.spock.service;

import com.spockatone.spock.dao.LotDao;
import com.spockatone.spock.entity.Lot;

import java.util.List;


public class LotService {
    private LotDao lotDao;

    public LotService(LotDao lotDao) {
        this.lotDao = lotDao;
    }

    public List<Lot> getLotsByPage(int pageNumber, int itemsOnPage){
        return lotDao.getLotsByPage(pageNumber, itemsOnPage);
    }
    public Lot getLotById(int id){
        return lotDao.getLotById(id);
    }
}
