package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    Transfer addNewTransfer(Transfer transfer, String username);

    int getAccountFromUsername(String username);

    public int getAccountIdFromUserId(int userId);

}
