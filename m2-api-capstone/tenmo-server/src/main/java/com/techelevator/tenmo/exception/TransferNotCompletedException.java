package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Transfer could not be completed.")
public class TransferNotCompletedException extends Exception {
    private static final long serialVersionUID = 1L;

    public TransferNotCompletedException() {
        super("Transfer could not be completed.");
    }
}
