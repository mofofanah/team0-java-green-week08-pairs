package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcAccountDAO implements AccountDAO {
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigDecimal retrieveAccountBalance(int userId) {

        BigDecimal balance = null;

        String sql = "select balance from accounts where user_id=?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);

        if (result.next()) {
            balance = result.getBigDecimal("balance");
        }


        return balance;
    }
}
