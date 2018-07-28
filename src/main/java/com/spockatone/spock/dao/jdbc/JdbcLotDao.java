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
    private DataSource dataSource;
    private static final String GET_BY_PAGE_SQL = "SELECT * FROM spock.public.lots LIMIT ? OFFSET ?;"; //TODO check

    private LotRawMapper lotRawMapper = new LotRawMapper();

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
                    lots.add(lotRawMapper.mapRaw(resultSet));
                }
                return lots;
            }
        } catch (SQLException e) {
            LOG.error("DB error during getLotsPyPage operation ", e);
            throw new RuntimeException(e);
        }
    }


}
