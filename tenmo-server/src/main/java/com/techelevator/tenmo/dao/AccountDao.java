package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getAccountBalanceByUserId(int userId);

    String getUsernameById(int accountId);

    int getAccountIdByUserId(int userId);

}
