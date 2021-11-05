package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.exception.TransferNotCompletedException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    TransferDAO transferDAO;

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers/", method = RequestMethod.POST)
    public Transfer sendBucks(@RequestBody Transfer transferToExecute) throws TransferNotCompletedException {
        return transferDAO.sendBucks(transferToExecute);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/transfers/{user_id}", method = RequestMethod.GET)
    public List<Transfer> retrieveTransferHistory(@PathVariable Long user_id) throws TransferNotCompletedException {
        return transferDAO.retrieveTransferHistory(user_id);
    }

//    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
//    @RequestMapping(path = "/transfers/pending", method = RequestMethod.GET)

}
