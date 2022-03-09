package com.techelevator.tenmo.dao;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;


public interface AccountDao {

    public BigDecimal viewAccountBalance(String name);

}
