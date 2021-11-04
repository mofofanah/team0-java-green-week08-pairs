package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
public class AccountController {

    @Autowired
    private AccountDAO dao;

    @RequestMapping(path = "/accounts/{user_id}", method = RequestMethod.GET )
    public Account retrieveAccountBalance(@PathVariable("user_id") int user_id) {
        return dao.retrieveAccountBalance(user_id);
    }

}
