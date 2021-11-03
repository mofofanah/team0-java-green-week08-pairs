package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController

public class AccountController {
 @Autowired
   private AccountDAO dao;
    @RequestMapping(path = "/accounts", method = RequestMethod.GET )
    public BigDecimal retrieveAccountBalance(int userId, Principal principal) {
//        principal.

        return dao.retrieveAccountBalance(userId);
    }
}
