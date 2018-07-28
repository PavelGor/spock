package com.spockatone.spock.service;

import com.spockatone.spock.dao.LotDao;
import com.spockatone.spock.entity.Lot;

import java.util.List;


public class LotService {
    private LotDao lotDao;
    private static int itemsPerPage;

    public LotService(LotDao lotDao) {
        this.lotDao = lotDao;
    }

    public List<Lot> getLotsByPage(int pageNumber) {
        int lotsPagesCount = getLotsPagesCount();
        pageNumber = pageNumber > lotsPagesCount ? lotsPagesCount : pageNumber;
        pageNumber = pageNumber < 1 ? 1 : pageNumber;
        return lotDao.getLotsByPage(pageNumber, itemsPerPage);
    }

    public int getLotsCount() {
        return lotDao.getLotsCount();
    }

    public int getLotsPagesCount() {
        int lotsCount = getLotsCount(); //15
        int lotsPagesCount = lotsCount / itemsPerPage; //15/6
        lotsPagesCount = (lotsPagesCount * itemsPerPage) < lotsCount ? lotsPagesCount + 1 : lotsPagesCount;
        return lotsPagesCount;
    }

    public static void setItemsPerPage(String itemsPerPageStr) {
        try {
            itemsPerPage = Integer.parseInt(itemsPerPageStr);

        } catch (Exception e) {
            itemsPerPage = 6;
        }
    }
    public Lot getLotById(int id){
        return lotDao.getLotById(id);
    }
}
