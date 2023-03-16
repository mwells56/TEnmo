package com.techelevator.tenmo.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class Account {

    @Autowired
    private int accountId;
    @Autowired
    private int userId;
    @Autowired
    private BigDecimal balance;
    @Autowired
    private String username;

    public Account () {

    }

    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", userId=" + userId +
                ", balance=" + balance +
                ", username='" + username + '\'' +
                '}';
    }

}
