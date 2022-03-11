package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;

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

    @Override
    public BigDecimal updateFromAccount(Transfer transfer, String username) {

        BigDecimal currentBalance = viewAccountBalance(username);
        BigDecimal updatedBalance =  currentBalance.subtract(transfer.getTransferAmount());

        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";

        jdbcTemplate.update(sql, BigDecimal.class, updatedBalance, getAccountFromUsername(username));

        return updatedBalance;
    }

    @Override
    public BigDecimal updateToAccount(Transfer transfer) {

        BigDecimal currentBalance = viewAccountBalance(getUsernameFromUserId(transfer.getAccountTo()));
        BigDecimal updatedBalance = currentBalance.add(transfer.getTransferAmount());

        String sql = "UPDATE account SET balance = ? WHERE user_id = ?";

        jdbcTemplate.update(sql, BigDecimal.class, updatedBalance, transfer.getAccountTo());

        return updatedBalance;
    }

    private int getAccountFromUsername(String username) {
        String sql = "SELECT account_id FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username = ?";

        Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, username);

        return accountId;
    }

    private String getUsernameFromUserId(int userId) {
        String sql = "SELECT username FROM tenmo_user WHERE user_id = ?";

        String username = jdbcTemplate.queryForObject(sql, String.class, userId);

        return username;
    }
}
