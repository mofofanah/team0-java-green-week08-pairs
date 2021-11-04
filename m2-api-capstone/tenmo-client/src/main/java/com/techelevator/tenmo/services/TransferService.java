package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {

    private String API_BASE_URL = "";
    private String AUTH_TOKEN = "";
    private RestTemplate restTemplate = new RestTemplate();

    public TransferService(String API_BASE_URL) {
        this.API_BASE_URL = API_BASE_URL;
    }


    public Transfer sendBucks(Long userId, Long targetAccountId, BigDecimal amount) {

        return restTemplate.exchange(API_BASE_URL + "transfers/" , HttpMethod.POST, this.makeAuthEntity(), Transfer.class).getBody();
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
