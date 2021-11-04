package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {

    public Account retrieveAccountBalance(int userId);
}
