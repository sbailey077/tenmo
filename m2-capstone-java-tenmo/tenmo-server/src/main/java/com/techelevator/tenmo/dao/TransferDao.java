package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    public Transfer addNewTransfer(Transfer transfer);

    public int getAccountIdFromUser(Transfer transfer);
}
