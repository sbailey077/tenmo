package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer addNewTransfer(Transfer transfer) {

        String sql = "INSERT INTO transfer(" +
                "transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                " VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";

        int id = jdbcTemplate.queryForObject(sql, int.class, transfer.getTypeID(), transfer.getStatusID(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getTransferAmount());

        transfer.setTransferId(id);
        // come back later if we need to add something

        return transfer;
    }

    @Override
    public int getAccountIdFromUser(Transfer transfer) {
        String sql = "SELECT account_id FROM account WHERE  user_id = ?";

        int id = jdbcTemplate.queryForObject(sql, int.class, transfer.getAccountTo());

        return id;
    }

}
