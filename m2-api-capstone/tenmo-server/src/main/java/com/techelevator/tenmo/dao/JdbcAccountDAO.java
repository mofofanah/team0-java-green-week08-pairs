package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Component
public class JdbcAccountDAO implements AccountDAO {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Account retrieveAccountBalance(int userId) {

        Account account = new Account();

        String sql = "SELECT accounts.account_id, accounts.user_id, accounts.balance, users.username " +
                "FROM accounts " +
                "JOIN users ON accounts.user_id = users.user_id " +
                "WHERE accounts.user_id = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);

        if (result.next()) {
            account.setAccountId(result.getLong("account_id"));
            account.setUserId(result.getLong("user_id"));
            BigDecimal balance = result.getBigDecimal("balance");
            account.setBalance(balance);
            account.setUserName(result.getString("username"));
        }

        return account;
    }

}
