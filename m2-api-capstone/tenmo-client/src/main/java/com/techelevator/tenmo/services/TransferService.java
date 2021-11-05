package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService {

    private String API_BASE_URL = "";
    private String AUTH_TOKEN = "";
    private RestTemplate restTemplate = new RestTemplate();

    public TransferService(String API_BASE_URL) {
        this.API_BASE_URL = API_BASE_URL;
    }


    public Transfer sendBucks(Long userId, Long targetAccountId, BigDecimal amount) {
        Transfer transfer = this.makeSendTransfer(userId, targetAccountId, amount);
        HttpEntity entity = this.makeTransferEntity(transfer);
        return restTemplate.exchange(API_BASE_URL + "transfers/" , HttpMethod.POST, entity, Transfer.class).getBody();
    }

    public List<Transfer> retrieveTransferHistory(Long userId) {

        HttpEntity entity = this.makeAuthEntity();
        Transfer[] transferArray= restTemplate.exchange(API_BASE_URL + "transfers/" + userId, HttpMethod.GET, entity, Transfer[].class).getBody();

        List<Transfer> listOfTransfers = Arrays.asList(transferArray);

        return listOfTransfers;
    }





    public Transfer makeSendTransfer(Long userId, Long targetAccountId, BigDecimal amount) {
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(userId);
        transfer.setAccountTo(targetAccountId);
        transfer.setAmount(amount);
        return transfer;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

    public void setAUTH_TOKEN(String AUTH_TOKEN) {
        this.AUTH_TOKEN = AUTH_TOKEN;
    }

}
