package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDAO {

    public List<Transfer> retrieveTransferHistory(int userId);
    public List<Transfer> retrievePendingRequests(int userId);
    public Transfer sendBucks (Transfer transfer);
}
