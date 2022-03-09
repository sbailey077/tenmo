package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController

public class TEnmoController {

    private UserDao userDao;
    private AccountDao accountDao;

    public TEnmoController(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    @PreAuthorize("permitAll")
    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal viewAccountBalance(Principal principal) {
        return accountDao.viewAccountBalance(principal.getName());
    }



}
