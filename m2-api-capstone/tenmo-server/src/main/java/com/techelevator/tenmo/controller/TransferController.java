package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class TransferController {

    @Autowired
    TransferDAO transferDAO;

    @RequestMapping(path = "/transfers/", method = RequestMethod.POST)
    public Transfer sendBucks(@RequestBody Transfer transferToExecute) {
        return transferDAO.sendBucks(transferToExecute);
    }


}
