package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer addNewTransfer(Transfer transfer, String username);

    int getAccountFromUsername(String username);

    int getAccountIdFromUserId(int userId);

    List<Transfer> getTransfersTo(String username);

    List<Transfer> getTransfersFrom(String username);

    List<Transfer> getAllTransfers(String username);

}
