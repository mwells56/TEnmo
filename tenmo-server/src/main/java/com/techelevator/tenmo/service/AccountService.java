package com.techelevator.tenmo.service;

import com.techelevator.tenmo.model.OtherUser;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    List<OtherUser> otherUsers (String username);

    BigDecimal getAccountBalance(String username);

}
