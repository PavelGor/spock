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

    private static final String GET_BY_PAGE_SQL = "SELECT * FROM spock.public.lots LIMIT ? OFFSET ?;"; //TODO check spock.public.lots
    private static final String GET_LOTS_COUNT_SQL = "SELECT COUNT(*) from lots;";
    private static final String GET_BY_ID_SQL = "SELECT * FROM spock.public.lots where ID = ?;";
    private static final LotRawMapper LOT_RAW_MAPPER = new LotRawMapper();

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

    public Lot getLotById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    LOG.warn("no lot found with id = " + id);
                    throw new RuntimeException("no lot found with id = " + id);
                }
                return LOT_RAW_MAPPER.mapRaw(resultSet);
            }
        } catch (SQLException e) {
            LOG.error("no lot found with id = " + id, e);
            throw new RuntimeException(e);
        }
    }
}
