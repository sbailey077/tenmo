package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController

public class TEnmoController {

    private UserDao userDao;
    private AccountDao accountDao;
    private TransferDao transferDao;

    public TEnmoController(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }


    @RequestMapping(path = "/username/balance", method = RequestMethod.GET)
    public BigDecimal viewAccountBalance(Principal principal) {
        return accountDao.viewAccountBalance(principal.getName());
    }


    @RequestMapping(path = "/username", method = RequestMethod.GET)
    public List<User> getUsers(Principal principal) {
        return userDao.getUserIdAndName(principal.getName());
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer enterNewTransfer(Principal principal,@RequestBody Transfer transfer) {
        return transferDao.addNewTransfer(transfer, principal.getName());
    }

    @RequestMapping(path = "/account/{username}/transfer", method = RequestMethod.GET)
    public List<Transfer> getTransfers(@PathVariable String username, Principal principal) {
        return transferDao.getTransfers(principal.getName());
    }

}
