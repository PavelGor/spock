package com.spockatone.spock.dao.jdbc;

import com.spockatone.spock.dao.LotDao;
import com.spockatone.spock.dao.jdbc.mapper.LotRawMapper;
import com.spockatone.spock.entity.Lot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcLotDao implements LotDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcLotDao.class);
    private static final String GET_BY_PAGE_SQL = "SELECT * FROM lots LIMIT ? OFFSET ?;"; //TODO check
    private static final String GET_LOTS_COUNT_SQL = "SELECT COUNT(*) from lots;";
    private final static LotRawMapper LOT_RAW_MAPPER = new LotRawMapper();

    private DataSource dataSource;

    public JdbcLotDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Lot> getLotsByPage(int page, int itemsPerPage) {
        List<Lot> lots;
        int pageOffset = itemsPerPage * (page - 1);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_PAGE_SQL)) {
            preparedStatement.setInt(1, itemsPerPage);
            preparedStatement.setInt(2, pageOffset);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                lots = new ArrayList<>();
                while (resultSet.next()) {
                    lots.add(LOT_RAW_MAPPER.mapRaw(resultSet));
                }
                return lots;
            }
        } catch (SQLException e) {
            LOG.error("DB error during obtaining lots from db", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getLotsCount() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_LOTS_COUNT_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            LOG.error("DB error during obtaining lots count from db", e);
            throw new RuntimeException(e);
        }    }


}
