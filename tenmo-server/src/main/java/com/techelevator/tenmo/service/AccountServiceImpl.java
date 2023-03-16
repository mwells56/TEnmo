package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.OtherUser;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;

    @Override
    public List<OtherUser> otherUsers(String username) {
        int userId = userDao.findIdByUsername(username);
        List<User> usersList = userDao.getAllOtherUsers(userId);

        List<OtherUser> otherUsers = new ArrayList<>();

        for (User user : usersList) {
            OtherUser otherUser = new OtherUser();

            otherUser.setUsername(user.getUsername());
            otherUser.setUserId(user.getId());

            otherUsers.add(otherUser);
        }
        return otherUsers;
    }

    @Override
    public BigDecimal getAccountBalance(String username) {
        int userId = userDao.findIdByUsername(username);
        return accountDao.getAccountBalanceByUserId(userId);
    }

}
