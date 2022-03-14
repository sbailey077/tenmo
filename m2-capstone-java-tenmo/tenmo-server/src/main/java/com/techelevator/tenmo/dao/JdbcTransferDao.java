package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
        updateSenderAccount(transfer.getTransferAmount(), getAccountFromUsername(username));
        updateRecipientAccount(transfer.getTransferAmount(), transfer.getAccountTo());

        String sql = "INSERT INTO transfer(" +
                "transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                " VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";

        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTypeID(), transfer.getStatusID(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getTransferAmount());

        transfer.setTransferId(id);

        return transfer;
    }

    @Override
    public List<Transfer> getTransfersFrom(String username) {
        List<Transfer> transfers = new ArrayList<Transfer>();
        int accountId = getAccountFromUsername(username);

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE account_from = ?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, accountId);

        while(row.next()) {
            Transfer transfer = mapRowToTransfer(row);
            transfer.setUsernameFrom(getUsernameFromAccount(accountId));
            transfer.setUsernameTo(getUsernameFromAccount(transfer.getAccountTo()));
            transfer.setTransferType("To: ");
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public List<Transfer> getTransfersTo(String username) {
        List<Transfer> transfers = new ArrayList<Transfer>();
        int accountId = getAccountFromUsername(username);

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
        "FROM transfer WHERE account_to = ?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, accountId);

        while (row.next()) {
            Transfer transfer = mapRowToTransfer(row);
            transfer.setUsernameFrom(getUsernameFromAccount(transfer.getAccountFrom()));
            transfer.setUsernameTo(getUsernameFromAccount(accountId));
            transfer.setTransferType("From: ");
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public List<Transfer> getAllTransfers(String username) {
        List<Transfer> transfersFrom = getTransfersFrom(username);
        List<Transfer> transfersTo = getTransfersTo(username);
        List<Transfer> allTransfers = new ArrayList<Transfer>();

        allTransfers.addAll(transfersFrom);
        allTransfers.addAll(transfersTo);

        return allTransfers;
    }

    @Override
    public Transfer getRequestedTransfer(int transferId) {
        Transfer transfer = new Transfer();

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE transfer_id = ?";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, transferId);

        while(row.next()) {
            transfer = mapRowToTransfer(row);
            transfer.setUsernameFrom(getUsernameFromAccount(transfer.getAccountFrom()));
            transfer.setUsernameTo(getUsernameFromAccount(transfer.getAccountTo()));
            transfer.setTransferType("Send");
            transfer.setTransferStatus("Approved");
        }
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

    private String getUsernameFromAccount(int accountId) {
        String sql = "SELECT tenmo_user.username FROM " +
                "account JOIN tenmo_user ON tenmo_user.user_id = account.user_id " +
                "WHERE account.account_id = ?";

        String username = jdbcTemplate.queryForObject(sql, String.class, accountId);

        return username;
    }

    private void updateSenderAccount(BigDecimal amountToSubtract, int accountFrom) {

        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";

        jdbcTemplate.update(sql, amountToSubtract, accountFrom);
    }

    private void updateRecipientAccount (BigDecimal amountToAdd, int accountTo) {

        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";

        jdbcTemplate.update(sql, amountToAdd, accountTo);
    }


    private Transfer mapRowToTransfer(SqlRowSet row) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(row.getInt("transfer_id"));
        transfer.setTypeID(row.getInt("transfer_type_id"));
        transfer.setStatusID(row.getInt("transfer_status_id"));
        transfer.setAccountFrom(row.getInt("account_from"));
        transfer.setAccountTo(row.getInt("account_to"));
        transfer.setTransferAmount(row.getBigDecimal("amount"));

        return transfer;
    }




}
