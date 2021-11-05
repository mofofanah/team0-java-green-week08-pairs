package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class TransferController {

    @Autowired
    TransferDAO transferDAO;

    @RequestMapping(path = "/transfers/", method = RequestMethod.POST)
    public Transfer sendBucks(@RequestBody Transfer transferToExecute) {
        return transferDAO.sendBucks(transferToExecute);
    }

    @RequestMapping(path = "/transfers/{user_id}", method = RequestMethod.GET)
    public List<Transfer> retrieveTransferHistory(@PathVariable Long user_id) {

        return transferDAO.retrieveTransferHistory(user_id);
    }
}
