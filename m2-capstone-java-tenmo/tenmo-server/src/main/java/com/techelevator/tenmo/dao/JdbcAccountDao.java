package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal viewAccountBalance(String name) {
        String sql = "SELECT balance FROM account JOIN tenmo_user" +
                " ON tenmo_user.user_id = account.user_id WHERE username = ?";
        BigDecimal accountBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, name);

        return accountBalance;
    }
}
