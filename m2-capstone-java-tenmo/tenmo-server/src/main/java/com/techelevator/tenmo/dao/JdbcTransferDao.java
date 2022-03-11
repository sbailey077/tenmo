package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer addNewTransfer(Transfer transfer, String username) {

        transfer.setAccountTo(getAccountIdFromUserId(transfer.getAccountTo()));
        transfer.setAccountFrom(getAccountFromUsername(username));

        String sql = "INSERT INTO transfer(" +
                "transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                " VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";

        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTypeID(), transfer.getStatusID(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getTransferAmount());

        transfer.setTransferId(id);

        return transfer;
    }


    public int getAccountIdFromUserId(int userId) {
        String sql = "SELECT account_id FROM account WHERE user_id = ?";

        Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);

        return accountId;
    }

    public int getAccountFromUsername(String username) {
        String sql = "SELECT account_id FROM account JOIN tenmo_user ON tenmo_user.user_id = account.user_id WHERE username = ?";

        Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, username);

        return accountId;
    }





}
