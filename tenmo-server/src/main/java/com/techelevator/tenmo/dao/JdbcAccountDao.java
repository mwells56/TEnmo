package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getAccountBalanceByUserId(int userId) {
        String sql = "SELECT balance " +
                "FROM account " +
                "JOIN tenmo_user " +
                "ON tenmo_user.user_id = account.user_id " +
                "WHERE tenmo_user.user_id = ?;";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    @Override
    public int getAccountIdByUserId(int userId) {
        String sql = "SELECT account_id " +
                "FROM account " +
                "JOIN tenmo_user " +
                "ON tenmo_user.user_id = account.user_id " +
                "WHERE tenmo_user.user_id = ?;";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    @Override
    public String getUsernameById(int accountId) {
        String sql = "SELECT username FROM tenmo_user JOIN account ON account.user_id = tenmo_user.user_id WHERE account_id = ?;";
        return jdbcTemplate.queryForObject(sql, String.class, accountId);
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
