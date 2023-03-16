package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.OtherUser;
import com.techelevator.tenmo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/account")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(Principal principal) {
        return accountService.getAccountBalance(principal.getName());
    }

    @RequestMapping(path = "/getAll", method = RequestMethod.GET)
    public List<OtherUser> getAllOtherAccounts(Principal principal) {
        return accountService.otherUsers(principal.getName());
    }
}
